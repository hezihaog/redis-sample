package com.zh.redissample.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jedis测试类
 */
public class JedisTest {
    public static void main(String[] args) {
//        testString();
//        testHash();
//        testList();
//        testSet();
//        testSortedSet();
        testPool();
    }

    /**
     * 测试字符串
     */
    public static void testString() {
        //获取连接
        Jedis jedis = new Jedis("localhost", 6379);
        String key = "username";
        //存储
        jedis.set(key, "zhangsan");
        //获取
        String value = jedis.get(key);
        System.out.println("测试String获取：key = " + key + " value = " + value);
        //20秒后，自动过期，将该键值对移除（key = activecode , value = hehe）
        jedis.setex("activecode", 20, "hehe");
        //关闭连接
        jedis.close();
    }

    /**
     * 测试Map，哈希键值对
     */
    public static void testHash() {
        Jedis jedis = new Jedis("localhost", 6379);
        //存储
        String key = "user";
        jedis.hset(key, "name", "lishi");
        jedis.hset(key, "age", "18");
        jedis.hset(key, "gender", "female");
        //获取
        String name = jedis.hget(key, "name");
        System.out.println("测试Hash，key = " + key + "，field = name" + name);
        //获取所有
        Map<String, String> value = jedis.hgetAll(key);
        for (Map.Entry<String, String> entry : value.entrySet()) {
            System.out.println("测试Hash，获取所有" + "field = " + entry.getKey() + "，value" + entry.getValue());
        }
        jedis.close();
    }

    /**
     * 测试List，链表，允许重复
     */
    public static void testList() {
        Jedis jedis = new Jedis("localhost", 6379);
        String key = "myList";
        //存储
        jedis.lpush(key, "a", "b", "c");//从左边存
        jedis.rpush(key, "a", "b", "c");//从右边存
        //获取所有
        List<String> myList = jedis.lrange(key, 0, -1);
        System.out.println("测试List，获取所有" + myList);
        //获取左边第一个
        String element1 = jedis.lpop(key);
        System.out.println("element1 = " + element1);
        //获取右边第一个
        String element2 = jedis.rpop(key);
        System.out.println("element2 = " + element2);
        //再次获取所有
        myList = jedis.lrange(key, 0, -1);
        System.out.println("测试List，获取所有" + myList);
        jedis.close();
    }

    /**
     * 测试集合，不允许重复
     */
    public static void testSet() {
        Jedis jedis = new Jedis("localhost", 6379);
        String key = "mySet";
        //存储
        jedis.sadd(key, "java", "php", "c++");
        //获取
        Set<String> mySet = jedis.smembers(key);
        System.out.println(mySet);
        //移除
        jedis.srem(key, "java");
        //再次获取
        mySet = jedis.smembers(key);
        System.out.println(mySet);
        jedis.close();
    }

    /**
     * 测试有序集合，不允许重复，并自动排序
     */
    public static void testSortedSet() {
        Jedis jedis = new Jedis("localhost", 6379);
        String key = "mySortedSet";
        //存储
        jedis.zadd(key, 50, "Rose");
        jedis.zadd(key, 3, "Wally");
        jedis.zadd(key, 30, "Barry");
        //获取
        Set<String> mySortedSet = jedis.zrange(key, 0, -1);
        System.out.println("测试SortedSet，获取所有：" + mySortedSet);
        //移除
        jedis.zrem(key, "Wally");
        //再次获取
        mySortedSet = jedis.zrange(key, 0, -1);
        System.out.println("测试SortedSet，获取所有：" + mySortedSet);
        jedis.close();
    }

    /**
     * 测试连接池
     */
    public static void testPool() {
        //连接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(50);
        //最大空闲连接
        config.setMaxIdle(10);
        JedisPool pool = new JedisPool(config, "localhost", 6379);
        //获取连接
        Jedis jedis = pool.getResource();
        String key = "myName";
        jedis.set(key, "zihe");
        String value = jedis.get(key);
        System.out.println("测试连接池，获取数据" + "key = " + key + "，value = " + value);
        //归还连接
        jedis.close();
    }
}