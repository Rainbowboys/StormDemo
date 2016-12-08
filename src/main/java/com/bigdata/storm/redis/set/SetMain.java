package com.bigdata.storm.redis.set;

import com.bigdata.storm.redis.InitRedisConnection;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Rainbow on 2016/12/7.
 */
public class SetMain {


    public static void main(String[] args) {

        Jedis jedis = InitRedisConnection.getConnection();
        //河南武林人物登记表---杜绝冒名顶替的情况
        String[] daxias = new String[]{"郭靖", "黄蓉", "令狐冲", "杨过", "林冲",
                "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳", "张无忌"
                , "王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉"
                , "韦小宝", "王语嫣", "周芷若", "峨眉师太", "慕容复", "郭靖", "乔峰", "王重阳"};

        jedis.sadd("biwu:dengji", daxias);

        Set<String> set = jedis.smembers("biwu:dengji");
        for (String name : set) {
            System.out.print(name + " ");
        }
        System.out.println();

        if (!jedis.sismember("biwu:dengji", "井中月")) {
            System.out.println("大侠 井中月尚未登记.");
        }

        //计算一个set中有多少元素

        Long totalNum = jedis.scard("biwu:dengji");
        System.out.println("有" + totalNum + " 位大侠已经登记了！");
        System.out.println();


        //大侠井中月没有来，是因为报名参与另外一个会议 国际武林大会
        String[] daxiaArr = new String[]{"王语嫣", "周芷若", "峨眉师太", "慕容复", "郭靖", "乔峰", "井中月"};

        jedis.sadd("guoji:dengji", daxiaArr);

        Set<String> xindaxias = jedis.smembers("guoji:dengji");
        for (String name : xindaxias) {
            System.out.print(name + "--- ");  //集合的特点：无序、无重复元素
        }
        System.out.println();

        //计算两个Set之间的交集

        Set<String> users = jedis.sinter("biwu:dengji", "guoji:dengji");
        for (String name : users) {
            System.out.print(name + "--- ");
        }
        System.out.println();

        //求集合的交集
        users = jedis.sunion("biwu:dengji", "guoji:dengji");
        for (String name : users) {
            System.out.print(name + "--- ");
        }
        System.out.println();
        System.out.println("井中月出来了");

        //求集合的 差集

        users = jedis.sdiff("biwu:dengji", "guoji:dengji");
        for (String name : users) {
            System.out.print(name + "--- ");
        }
        System.out.println();

        //将两个集合计算出来的差集保存起来，升级为超级Vip
        jedis.sdiffstore("vipdaxia","biwu:dengji", "guoji:dengji");
        for (String name : jedis.smembers("vipdaxia")) {
            System.out.print(name + " ");
        }
    }
}
