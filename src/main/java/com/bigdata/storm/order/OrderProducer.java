package com.bigdata.storm.order;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import kafka.producer.ProducerConfig;

import java.text.ParseException;
import java.util.Properties;

/**
 * Created by Rainbow on 2016/12/6.
 */
public class OrderProducer {


    public static void main(String[] args) throws ParseException, InterruptedException {
        String TOPIC="Order";
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "zookeeper01:9092,zookeeper01:9092,zookeeper01:9092");
        props.put("request.required.acks", "1");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));

         for (int messageNO=1;messageNO<1000000;messageNO++){


             producer.send(new KeyedMessage<String,String>(TOPIC,messageNO+"",new OrderInfo().random()));
             Thread.sleep(100);

         }


    }

}
