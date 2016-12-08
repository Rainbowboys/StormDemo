package com.bigdata.storm.redis.map;

import com.bigdata.storm.redis.InitRedisConnection;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rainbow on 2016/12/7.
 */
public class MapMain {

    public static void main(String[] args) {
        Jedis jedis = InitRedisConnection.getConnection();

        //创建一个对象

        jedis.hset("xiaohming", "姓名", "无名大侠");
        jedis.hset("xiaohming", "年龄", "18");
        jedis.hset("xiaohming", "爱好", "吃饭");

        Map<String, String> xiaoming = jedis.hgetAll("xiaohming");

        for (Map.Entry entry:xiaoming.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());

        }


        String name = jedis.hget("xiaohming", "姓名");
        System.out.println(name + " ");


        //获取所有字段key

        Set<String> hkeys = jedis.hkeys("xiaohming");
        for (String key:hkeys){

            System.out.println(key);
        }

        //获取所有字段values

        List<String> hvals = jedis.hvals("xiaohming");
        for (String val:hvals){

            System.out.println(val);
        }

        jedis.hincrBy("xiaohming","年龄",10);
        System.out.println(jedis.hget("xiaohming","年龄"));


    }
}
