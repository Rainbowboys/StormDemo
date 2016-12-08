package com.bigdata.storm.redis.map;

import com.bigdata.storm.redis.InitRedisConnection;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rainbow on 2016/12/7.
 */
public class Cart {

    private Jedis jedis;

    public Cart() {

        jedis = InitRedisConnection.getConnection();
    }

    public Cart(Jedis jedis) {
        this.jedis = jedis;
    }

    public static void main(String[] args) {

        initData();

        //创建购物车
        Cart cart = new Cart();

        cart.updateProduct2Cart("xiaozhang", "1645139266", 100);
        cart.updateProduct2Cart("xiaozhang", "1788744384", 100);
        cart.updateProduct2Cart("xiaozhang", "1645139266", -200);
        List<Product> xiaozhangCart = cart.getProductsByUserName("xiaozhang");
        for (Product product : xiaozhangCart) {

            System.out.println(product);
        }

    }

   /*更新用户购物车商品信息*/

    public void updateProduct2Cart(String userName, String productId, int num) {
        if (num > 0) {
            jedis.hincrBy("shop:cart" + userName, productId, num);
        }

    }

    public List<Product> getProductsByUserName(String userName) {

        ArrayList<Product> products = new ArrayList<Product>();
        Map<String, String> productMap = jedis.hgetAll("shop:cart" + userName);

        if (productMap == null || productMap.size() == 0) {
            return products;
        }
        /*获取商品信息*/
        for (Map.Entry entry : productMap.entrySet()) {
            Product product = new Product();
            product.setId((String) entry.getKey());//设置商品ID
            int num = Integer.valueOf((String) entry.getValue());
            product.setNum(num > 0 ? num : 0);//设置商品数量
            complementOtherField(product);//补全商品的其他信息
            products.add(product);
        }

        return products;
    }

    private void complementOtherField(Product product) {
        String productString = jedis.get("shop:product:" + product.getId());
        Product productJson = new Gson().fromJson(productString, Product.class);
        if (productJson != null) {
            product.setName(productJson.getName());
            product.setPrice(productJson.getPrice());
        }
    }

    /*初始化商品信息 准备商品数据*/

    public static void initData() {
        System.out.println("-----------初始化商品信息 准备商品数据--------------");

        Jedis jedis = InitRedisConnection.getConnection();
        /*准备商品*/
        Product product1 = new Product("1645139266", "战地鳄2015秋冬新款马甲可脱卸帽休闲时尚无袖男士羽绒棉外套马甲", new BigDecimal("168"));
        Product product2 = new Product("1788744384", "天乐时 爸爸装加厚马甲秋冬装中年大码男士加绒马夹中老年坎肩老年人", new BigDecimal("40"));
        Product product3 = new Product("1645080454", "战地鳄2015秋冬新款马甲可脱卸帽休闲时尚无袖男士羽绒棉外套马甲", new BigDecimal("230"));

        jedis.set("shop:product:" + product1.getId(), new Gson().toJson(product1));
        jedis.set("shop:product:" + product2.getId(), new Gson().toJson(product2));
        jedis.set("shop:product:" + product3.getId(), new Gson().toJson(product3));

        System.out.println("-----------商品数据--------------");

        Set<String> keys = jedis.keys("shop:product:*");
        for (String key : keys) {

            String productString = jedis.get(key);
            Product product = new Gson().fromJson(productString, Product.class);
            System.out.println(product);
        }
        System.out.println("========================用户购物车信息如下===========================");
    }

}
