package net.opentsdb.tsd;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import net.opentsdb.core.Aggregator;
import net.opentsdb.core.Aggregators;
import net.opentsdb.core.Query;
import net.opentsdb.core.TSDB;
import net.opentsdb.core.Tags;
import net.opentsdb.stats.StatsCollector;
import net.opentsdb.uid.NoSuchUniqueName;


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
  
  private static final AtomicLong get_requests = new AtomicLong();
  private static final AtomicLong get_errors = new AtomicLong();
  private static final AtomicLong get_metrics = new AtomicLong();
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
        LOG.info("Put..." + metrics.size());
        //if (metrics.size() <= 10) 
        	//LOG.info("size of number is single-digits, may be misused in metric " + metrics.get(0).tags.toString());
        puts_requests.getAndAdd(metrics.size());
        puts_metrics.incrementAndGet();
        
        for (final Metric m : metrics) {
          try {
            tsdb.addPoint(m.metric, m.timestamp, (float)(m.value), m.tags).addErrback(
          		new Callback<Exception, Exception>() {
          			public Exception call(final Exception arg) {
          				LOG.error(m.metric + m.timestamp + m.value + m.tags);
          				System.out.println("\t" + "\t" + "\t" + "ErrInfo: " + arg.getMessage());
          				String metricsLeft = m.metric + " " + m.timestamp + " " + m.value + " " + m.tags.toString() + "\n";
          				
                  try {
                		final FileWriter out = new FileWriter("/var/log/tsdb/tsdb.dat.left", true);
                		try {                			
                			out.append(metricsLeft);                		
                		} finally {
                			out.close();
                	}
                } catch (FileNotFoundException e) {
                	LOG.error("Failed to create file " + e);
                } catch (IOException e) {
                	LOG.error("Failed to write file " + e);
                }
          				
          				
          				return arg;
          			}
            	});
          } catch (Exception e) {
              LOG.error("Put faild.", e);
              puts_errors.incrementAndGet();
          }
        }
        return true;
    }
    
   
    
    

    @Override
    public List<Spans> Get(QueryStr querystr)
    throws org.apache.thrift.TException {
    	List<Spans> Lspans = new ArrayList<Spans>();
        
      get_metrics.incrementAndGet();
        
      try {
      	GraphHandler gh = new GraphHandler();
	      Spans spans = gh.doThriftGet(tsdb, querystr);
	      Lspans.add(spans);
      } catch (Exception e) {
      	LOG.error("error while serving " + querystr + "querystr: " + e.getMessage());
      	LOG.error("trace " + e.fillInStackTrace());
      	e.printStackTrace();
      }

      System.out.println("siz");
      if (Lspans.get(0) == null) {
	      System.out.println("error");
	      System.out.println("is null");
	      return null;
      } else {
      	System.out.println("is null");
	      System.out.println("Eerror");
			}
      return Lspans;
      
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
