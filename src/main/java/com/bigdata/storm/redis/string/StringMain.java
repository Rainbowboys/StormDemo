package com.bigdata.storm.redis.string;

import com.bigdata.storm.redis.InitRedisConnection;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Rainbow on 2016/12/7.
 */
public class StringMain {

    public static void main(String[] args) throws InterruptedException {


        // Jedis jedis = InitRedisConnection.getConnection();
        Jedis jedis = new Jedis("192.168.145.200", 6379);
        jedis.set("name", "liudehua"); //插入一个名字，叫做刘德华
        System.out.println(jedis.get("name"));

        jedis.set("age", "17");
        jedis.incr("age");
        System.out.println(jedis.get("age"));

        jedis.decr("age");//自减一
        System.out.println(jedis.get("age"));

        //一次性插入多条
        jedis.mset("AAA", "Mysql数据库的操作"
                , "BBB", "熟悉LINXU操作系统"
                , "CCC", "熟悉SSH、SSM框架及配置"
                , "DDD", "熟悉Spring框架，mybatis框架，Spring IOC MVC的整合，Spring和Mybatis的整合");

        List<String> list = jedis.mget("AAA", "BBB", "CCC", "DDD");
        for(String name:list){
            System.out.println(name);
        }


        //设置字段的自动过期
        jedis.setex("wumai",10,"天上人间");

        while (jedis.exists("wumai")){

            System.out.println(jedis.get("wumai"));
            Thread.sleep(1000);
        }

        jedis.set("wumai", "我们活在仙境中");
        jedis.expire("wumai", 10); //让天上人间的感觉保持更长的时间
        while (jedis.exists("wumai")) {
            System.out.println("真是天上人间呀！");
            Thread.sleep(1000);
        }

    }

}
