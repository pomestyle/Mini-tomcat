package com.udeam.util;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 静态资源工具类
 */
public class ResourUtil {

    /**
     * 获取classes文件目录
     */
    private static URL url = ResourUtil.class.getClassLoader().getResource("\\\\");

    /**
     * 获取静态资源文件路径
     *
     * @param path
     * @return
     */
    public static String getStaticPath(String path) throws UnsupportedEncodingException {

        //获取目录的绝对路径
        try {
            String decode = URLDecoder.decode(url.getPath(), "UTF-8");
            String replace1 = decode.replace("\\", "/");
            String replace2 = replace1.replace("//", "");
            replace2 = replace2.substring(0, replace2.lastIndexOf("/")) + path;
            return replace2;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        URL path1 = ResourUtil.class.getClassLoader().getResource("\\");
        System.out.println(path1);
    }


    /**
     * 读取静态资源文件输入流
     *
     * @param inputStream
     */
    public static void readFile(InputStream inputStream, OutputStream outputStream) throws IOException {

        int count = 0;
        //读取请求信息
        while (count == 0) {
            count = inputStream.available();
        }

        int content = 0;


        //读取文件
        content = count;


        //输出头
        outputStream.write(HttpUtil.addHeadParam(content).getBytes());
        //输出内容
        long written = 0;
        int byteSize = 1024;
        byte[] b = new byte[byteSize];
        //读取
        while (written < content) {
            if (written + 1024 > content) {
                byteSize = (int) (content - written);
                b = new byte[byteSize];
            }
            inputStream.read(b);
            outputStream.write(b);
            outputStream.flush();
            written += byteSize;

        }


    }
}
