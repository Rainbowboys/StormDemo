package com.bigdata.storm.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 * Created by Rainbow on 2016/12/6.
 */
public class MyStormKafka {


    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder topologyBuilder=new TopologyBuilder();
        topologyBuilder.setSpout("KafkaSpout", new KafkaSpout(new SpoutConfig(new ZkHosts("zookeeper01:2181,zookeeper02:2181,zookeeper03:2181"), "Order", "/Order", "OrderTopic")), 1);
        topologyBuilder.setBolt("myBolt",new OrderBolt(),2).shuffleGrouping("KafkaSpout");

        Config config=new Config();
        config.setNumWorkers(2);

        LocalCluster localCluster=new LocalCluster();
       localCluster.submitTopology("myorderinfo",config,topologyBuilder.createTopology());
        //StormSubmitter.submitTopology("mywordcount", config, topologyBuilder.createTopology());

    }
}
