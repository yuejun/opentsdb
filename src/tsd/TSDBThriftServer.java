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

/**
 * ThriftServer - this class starts up a Thrift server which implements the
 * TSDB API specified in the TSDB.thrift IDL file.
 */
public class TSDBThriftServer {

  public TSDBThriftServer(final TSDB tsdb) {
    this.tsdb = tsdb;
  }

  private final TSDB tsdb;
  private static final Logger LOG = LoggerFactory.getLogger(TSDBThriftServer.class);
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
        LOG.info("Put..." + metrics.size());
        for (final Metric m : metrics) {
          tsdb.addPoint(m.metric, m.timestamp, (float)(m.value), m.tags);
        }
        LOG.info("Put success.");
        return true;
      } catch (Exception e) {
        LOG.error("Put faild.", e);
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
