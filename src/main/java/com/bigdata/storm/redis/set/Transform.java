package com.bigdata.storm.redis.set;

import com.bigdata.storm.redis.InitRedisConnection;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * Created by Rainbow on 2016/12/7.
 * 转化率
 *
 */
public class Transform {


    public static void main(String[] args) {

        Jedis jedis = InitRedisConnection.getConnection();
        //浏览某商品的用户
        jedis.sadd("viewUsers", "郭靖", "黄蓉", "令狐冲", "杨过", "林冲",
                "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳", "张无忌"
                , "王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉");

        //下单用户
        jedis.sadd("orderUsers", "郭靖", "黄蓉", "令狐冲", "杨过", "林冲",
                "鲁智深", "小女龙", "虚竹", "独孤求败", "乔峰", "虚竹", "段誉");
        //支付用户
        jedis.sadd("paymentUsers", "郭靖", "黄蓉", "令狐冲", "杨过", "独孤求败", "段誉");

        //浏览过商品的用户，有哪些下单了。

        jedis.sinterstore("view2order", "viewUsers", "orderUsers");
        Set<String> view2order = jedis.smembers("view2order");
        //计算浏览某商品的用户数量 和 既浏览又下单的用户的数量
        double viewUserNum = jedis.scard("viewUsers");
        double orderUserNum = jedis.scard("orderUsers");

        DecimalFormat formatter = new DecimalFormat("0.00");
        Double x = new Double(orderUserNum / viewUserNum);
        System.out.print("订单" + orderUserNum + "/浏览" + viewUserNum + "转化：" + formatter.format(x) + "     他们是：");

        for (String name:view2order){

            System.out.println(name+"---");
        }
        System.out.println();


        //浏览并且下单的用户，最终支付的转化

    }

}
