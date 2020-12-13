package com.udeam.v1;

import com.udeam.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 启动类入库
 * 用于启动tomcat
 * <p>
 * 版本1:返回指定字符串
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
            //InputStream inputStream = accept.getInputStream();
            //输出流
            OutputStream outputStream = accept.getOutputStream();
            String result = HttpUtil.resp_200("hello world ...");
            System.out.println(" ------ 响应返回内容 : " + result);
            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
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
