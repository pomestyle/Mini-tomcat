package com.udeam.v4;

import com.udeam.util.HttpUtil;
import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;
import com.udeam.v3.inteface.HttpServlet;

import java.io.IOException;

/**
 * 业务类servelt
 */
public class MyServlet extends HttpServlet {

    @Override
    public void init() throws Exception {
    }

    @Override
    public void doGet(Request request, Response response) {

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //动态业务请求
        String content = "<h2> GET 业务请求</h2>";
        try {
            response.outPutStr(HttpUtil.resp_200(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        //动态业务请求
        String content = "<h2> Post 业务请求</h2>";
        try {
            response.outPutStr(HttpUtil.resp_200(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void destory() throws Exception {

    }
}
