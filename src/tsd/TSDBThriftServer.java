package net.opentsdb.tsd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opentsdb.core.TSDB;
import net.opentsdb.stats.StatsCollector;

/**
 * ThriftServer - this class starts up a Thrift server which implements the
 * TSDB API specified in the TSDB.thrift IDL file.
 */
public class TSDBThriftServer {

  public TSDBThriftServer(final TSDB tsdb) {
    this.tsdb = tsdb;
  }

  /**
   * Collects the stats and metrics tracked by this instance.
   * @param collector The collector to use.
   */
  public static void collectStats(final StatsCollector collector) {
    collector.record("thrift.hbase.errors", hbase_errors, "type=hbase_errors");
    collector.record("thrift.puts.requests", puts_requests, "type=puts_requests");
    collector.record("thrift.puts.errors", puts_errors, "type=puts_errors");
    collector.record("thrift.puts.metrics", puts_metrics, "type=puts_metrics");
  }

  private final TSDB tsdb;
  private static final Logger LOG = LoggerFactory.getLogger(TSDBThriftServer.class);
  private static final AtomicLong hbase_errors = new AtomicLong();
  private static final AtomicLong puts_requests = new AtomicLong();
  private static final AtomicLong puts_errors = new AtomicLong();
  private static final AtomicLong puts_metrics = new AtomicLong();
  /**
   * The HBaseHandler is a glue object that connects Thrift RPC calls to the
   * HBase client API primarily defined in the HBaseAdmin and HTable objects.
   */
  public static class TSDBHandler implements TSDBService.Iface {
    public TSDBHandler(final TSDB tsdb) {
      this.tsdb = tsdb;
    }
    private final TSDB tsdb;

    @Override
    public boolean Put(List<Metric> metrics)
                       throws org.apache.thrift.TException {
      try {
        final class PutErrback implements Callback<Exception, Exception> {
          public Exception call(final Exception arg) {
            LOG.info("put: HBase error: " + arg.getMessage() + '\n');
            hbase_errors.incrementAndGet();
            return arg;
          }
          public String toString() {
            return "handle error thirft puts.";
          }
        }

        LOG.info("Put..." + metrics.size());
        puts_requests.getAndAdd(metrics.size());
        puts_metrics.incrementAndGet();
        for (final Metric m : metrics) {
          Deferred<Object> deferred = tsdb.addPoint(m.metric, m.timestamp, (float)(m.value), m.tags);
          deferred.addErrback(new PutErrback());
        }
        LOG.info("Put success.");
        return true;
      } catch (Exception e) {
        LOG.error("Put faild.", e);
        puts_errors.incrementAndGet();
        return false;
      }
    }
  }

  public void Start() throws Exception {
    final Thrd thread = new Thrd();
    thread.setDaemon(true);
    thread.start();
  }

  final class Thrd extends Thread {
    public Thrd() {
      super("TSDBThriftServer");
    }

    public void run() {
      // Get port to bind to
      int listenPort = 4243;
      TProtocolFactory protocolFactory;
      if (false) {
        protocolFactory = new TCompactProtocol.Factory();
      } else {
        protocolFactory = new TBinaryProtocol.Factory();
      }

      TSDBHandler handler = new TSDBHandler(tsdb);
      TSDBService.Processor processor = new TSDBService.Processor(handler);

      TServer server;
      try {
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(listenPort);
        TFramedTransport.Factory transportFactory = new TFramedTransport.Factory();

        TNonblockingServer.Args serverArgs = new TNonblockingServer.Args(serverTransport);
        serverArgs.processor(processor);
        serverArgs.transportFactory(transportFactory);
        serverArgs.protocolFactory(protocolFactory);
        server = new TNonblockingServer(serverArgs);
        server.serve();
      } catch (org.apache.thrift.transport.TTransportException e) {
      }
    }
  }   
}
