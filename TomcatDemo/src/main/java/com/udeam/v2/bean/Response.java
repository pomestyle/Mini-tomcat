package com.udeam.v2.bean;

import com.udeam.util.HttpUtil;
import com.udeam.util.ResourUtil;

import java.io.*;

/**
 * 封装的返回实体类
 */
public class Response {

    /**
     * 响应流
     */
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    //输出指定字符串
    public void outPutStr(String content) throws IOException {
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 输出静态资源
     * 根据url获取静态资源的绝对路径,然后通过文件流读取返回
     *
     * @param url
     * @throws IOException
     */
    public void outPutHtml(String url) throws IOException {
        //排除浏览器的/favicon.ico请求
        if (("/favicon.ico").equals(url)) {
            return;
        }
        //获取静态资源的绝对路径
        String abPath = ResourUtil.getStaticPath(url);
        //查询静态资源是否存在
        File file = new File(abPath);
        if (file.exists()) {
            //输出静态资源
            ResourUtil.readFile(new FileInputStream(abPath), outputStream);
        } else {
            //404
            try {
                outPutStr(HttpUtil.resp_404());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
