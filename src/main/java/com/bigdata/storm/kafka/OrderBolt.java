package com.bigdata.storm.kafka;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.bigdata.storm.order.OrderInfo;
import com.google.gson.Gson;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rainbow on 2016/12/6.
 */
public class OrderBolt extends BaseRichBolt {

    OutputCollector collector;
    JedisPool pool;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        this.collector = collector;
        /**
         * 获取jedis
         *
         */
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setMaxTotal(1000 * 100);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
          //连接池
         pool = new JedisPool(config, "192.168.145.200", 7000);


    }

    public void execute(Tuple tuple) {

        /**
         * 获取一个Jedis连接
         */

        Jedis jedis = pool.getResource();
        String line = new String((byte[])tuple.getValue(0));
       //将json串cheng 成 Orderinfo对象


        OrderInfo orderInfo=(OrderInfo) new Gson().fromJson(line, OrderInfo.class);

        //整个网站，各个业务线，各个品类，各个店铺，各个品牌，每个商品
        //获取整个网站的金额统计指标
        jedis.incrBy("totalAmount", orderInfo.getProductPrice());
        //更具商品ID获取业务线
       // String bid =  getBubyProductId(orderInfo.getProductId(),"b");
        //获取各个业务线的金额统计指标
       // jedis.incrBy("phoneAmount", orderInfo.getProductPrice());
        jedis.close();

    }

    private String getBubyProductId(String productId, String type) {
        Jedis jedis = pool.getResource();
        String bid = jedis.get(type);
        return bid;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("kafka"));

    }
}
