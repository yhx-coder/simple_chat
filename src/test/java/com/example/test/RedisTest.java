package com.example.test;

import com.example.config.Config;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author: ming
 * @date: 2021/11/3 16:51
 */
public class RedisTest {

    @Test
    public void testLpop(){
        Jedis jedis = new Jedis(Config.getRedisAddress(), Config.getRedisPort());
        String unreadMessageJson = jedis.lpop(String.valueOf(1));
        System.out.println(unreadMessageJson); // null
        System.out.println("nil".equals(unreadMessageJson)); // false
    }
}
