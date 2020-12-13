package com.udeam.v2;

import com.udeam.util.HttpUtil;
import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 启动类入库
 * 用于启动tomcat
 * <p>
 * 版本2:
 * <p>
 * 封装请求,以及返回对象
 */
public class Bootstrap {

    /**
     * 监听端口号
     * 用于启动socket监听的端口号
     */
    private int port = 8080;

    /**
     * 启动方法
     */
    public void start() throws IOException {
        //返回固定字符串到客户端
        ServerSocket socket = new ServerSocket(port);
        System.out.println("--------- start port : " + port);

        while (true) {
            Socket accept = socket.accept();
            //获取输入流
            InputStream inputStream = accept.getInputStream();
            //封装请求和响应对象
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());
            response.outPutHtml(request.getUrl());
        }

    }

    public static void main(String[] args) {

        try {
            new Bootstrap().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
