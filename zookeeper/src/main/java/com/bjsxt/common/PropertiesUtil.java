package com.bjsxt.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertiesUtil {

    /**
     * 读取zookeeper中信息
     * @return
     */
    public static Properties readPropery(){
        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/zookeeper.properties");
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = readPropery();
        String url = (String) properties.get("zookeeper.url");
        System.out.println(url);
    }
}
