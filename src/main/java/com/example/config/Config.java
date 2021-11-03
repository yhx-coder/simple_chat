package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: ming
 * @date: 2021/9/5 22:50
 */
public class Config {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(Config.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 传输常数设置
    public static int getPort(){
        String port = properties.getProperty("trans.port");
        if (port==null){
            return 8899;
        }
        return Integer.parseInt(port);
    }

    public static String getAddress(){
        String address = properties.getProperty("trans.address");
        if (address==null){
            return "localhost";
        }
        return address;
    }

    // 连接属性设置
    public static int getReaderIdleTime(){
        String readerIdleTime = properties.getProperty("conn.readerIdleTime");
        if (readerIdleTime==null){
            return 50;
        }
        return Integer.parseInt(readerIdleTime);
    }

    public static int getWriterIdleTime(){
        String writerIdleTime = properties.getProperty("conn.writerIdleTime");
        if (writerIdleTime==null){
            return 30;
        }
        return Integer.parseInt(writerIdleTime);
    }

    public static int getRedisPort(){
        String redisPort = properties.getProperty("redis.port");
        if (redisPort==null){
            return 6379;
        }
        return Integer.parseInt(redisPort);
    }

    public static String getRedisAddress(){
        String redisAddress = properties.getProperty("redis.address");
        if (redisAddress==null){
            return "localhost";
        }
        return redisAddress;
    }
}
