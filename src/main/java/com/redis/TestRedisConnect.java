package com.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by xwz on 2018/3/7.
 */
public class TestRedisConnect {

    public static  void main(String[] args){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        System.out.println(applicationContext);

        JedisPool pool = (JedisPool)applicationContext.getBean("jedisPool");
        System.out.println(pool);

        Jedis conn = null;
        try{
            conn = pool.getResource();
            conn.set("key1", "123456");

            System.out.println(conn.get("key1"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }

    }
}
