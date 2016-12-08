package com.bigdata.storm.wordcount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by Rainbow on 2016/12/4.
 */
public class MySpout extends BaseRichSpout {
    SpoutOutputCollector collector;

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        declarer.declare(new Fields("biaobai"));


    }

    /*初始化收集器方法*/
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    /*发射消息*/
    public void nextTuple() {
        collector.emit(new Values("i love xiao hui"));
    }
}
