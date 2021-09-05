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

    public static int getPort(){
        String port = properties.getProperty("trans.port");
        if (port==null){
            return 8899;
        }
        return Integer.parseInt(port);
    }
}
