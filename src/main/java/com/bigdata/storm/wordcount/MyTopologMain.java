package com.bigdata.storm.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created by Rainbow on 2016/12/4.
 */
public class MyTopologMain {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
         /*初始化工作*/
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MySpout(), 1);
        topologyBuilder.setBolt("mySplitBolt", new mySplitBolt(), 10).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("myCountBolt", new myCountBolt(), 2).fieldsGrouping("mySplitBolt", new Fields("word"));

        Config config = new Config();
        config.setNumWorkers(2);

            /*两种提交模式 本地和集群*/
      //  StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        LocalCluster localCluster=new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
