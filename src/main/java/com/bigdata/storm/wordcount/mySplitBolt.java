package com.bigdata.storm.wordcount;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;

import backtype.storm.topology.OutputFieldsDeclarer;


import backtype.storm.topology.base.BaseRichBolt;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by Rainbow on 2016/12/4.
 */
public class mySplitBolt extends BaseRichBolt {

    OutputCollector collector;


    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {

        this.collector=collector;

    }

    public void execute(Tuple tuple) {

        String line=tuple.getString(0);
        String[] wordArrs= line.split(" ");
        for (String word:wordArrs){
            collector.emit(new Values(word,1));
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word","num"));

    }
}
