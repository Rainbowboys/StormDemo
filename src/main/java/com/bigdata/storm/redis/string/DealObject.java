package com.bigdata.storm.redis.string;

import com.bigdata.storm.redis.InitRedisConnection;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * 1.直接字符串保存
 * 2.序列化保存
 * 3、json串保存
 */
public class DealObject {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("xiaozhang", 18);

        Jedis jedis = InitRedisConnection.getConnection();
        //toString保存
        jedis.set("xiaozhang", person.toString());
        System.out.println(jedis.get("xiaozhang"));

        //序列化保存

        jedis.set("xiaozhang".getBytes(), getByteByPerson(person));
        byte[] personbytes = jedis.get("xiaozhang".getBytes());
        Person p = getProductByBytes(personbytes);
        System.out.println("personName:" + p.getName() + "  personAge:" + p.getAge());


        //json 串
        jedis.set("xiaozhangjson", new Gson().toJson(person));
        Person p2 = (Person) new Gson().fromJson(jedis.get("xiaozhangjson"), Person.class);
        System.out.println("personName:" + p2.getName() + "  personAge:" + p2.getAge());

    }


    /**
     * 将byte 数组 还原成 对象
     *
     * @param personbytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Person getProductByBytes(byte[] personbytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bis = new ByteArrayInputStream(personbytes);
        ObjectInputStream inputStream = new ObjectInputStream(bis);
        return (Person) inputStream.readObject();


    }

    /**
     * 将对象转byte 数组
     *
     * @param person
     * @return
     * @throws IOException
     */
    private static byte[] getByteByPerson(Person person) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(arrayOutputStream);
        oos.writeObject(person);
        oos.flush();
        return arrayOutputStream.toByteArray();
    }
}
