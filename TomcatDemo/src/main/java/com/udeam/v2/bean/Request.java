package com.udeam.v2.bean;

import com.udeam.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 封装的请求实体类
 */
public class Request {
    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求url
     */
    private String url;

    /**
     * 输入流
     */
    public InputStream inputStream;

    public Request() {
    }

    //构造器输入流
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        //读取请求信息,封装属性
        int count = 0;
        //读取请求信息
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] b = new byte[count];
        inputStream.read(b);
        String reqStr = new String(b);
        System.out.println("请求信息 : " + reqStr);
        //根据http请求报文 换行符截取
        String[] split = reqStr.split("\\n");

        //获取第一行请求头信息
        String s = split[0];
        //根据空格进行截取请求方式和url
        String[] s1 = s.split(" ");
        System.out.println("method : " + s1[0]);
        System.out.println("url : " + s1[1]);

        this.method = s1[0];
        this.url = s1[1];

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
