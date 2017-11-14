package com.bdlog.demo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 用户发送数据的http工具类
 */
public class HttpRequestUtil {
    public static void sendData(String urlStr) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();//打开url连接
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("GET");
            System.out.println("发送的url:"+urlStr);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
