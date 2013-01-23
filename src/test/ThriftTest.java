package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.opentsdb.tsd.*;
import org.apache.thrift.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;

public class ThriftTest {
  public static void main(String args[]) {

    Map<String, String> tags = new HashMap<String, String>();
    tags.put("inout", "in");
    // tags.put("product", "*");
    tags.put("idcname", "*");
    Item item = new Item();
    item.aggregator = 0;
    item.downsample_agg = 1;
    item.downsample_time = "24h";
    item.metric = "flow_mirror";
    item.rate = true;
    item.tags = tags;

    List<Item> items = new ArrayList<Item>();
    items.add(item);

    QueryStr querystr = new QueryStr();
    querystr.endtime = System.currentTimeMillis() / 1000;
    querystr.starttime = querystr.endtime - 24 * 60 * 60;
    // querystr.starttime=1358393600;
    // querystr.endtime=1358393700;
    querystr.nocache = true;
    querystr.items = items;

    List<SpanGroup> spans = new ArrayList<SpanGroup>();
    Result result = new Result();
    try {
      // TTransport socket = new TSocket("10.79.48.253", 4243);
      TTransport socket = new TSocket("10.217.12.119", 4243);
      TFramedTransport transport = new TFramedTransport(socket);
      TProtocol protocol = new TBinaryProtocol(transport);
      TSDBService.Client client = new TSDBService.Client(protocol);
      transport.open();
      System.out.println(querystr);
      result = client.Get(querystr);
      if (result == null) {
        System.out.println("null");
        System.out.println(result);
      } else {
        System.out.println(result);
      }
      transport.close();
    } catch (TException e) {
      if (e instanceof TApplicationException
          && ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
        System.out.println("The result of hello is NULL");

      } else {
        e.printStackTrace();
      }
    }
  }
}