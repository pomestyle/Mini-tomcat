package com.udeam.v5;

import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;
import com.udeam.v3.inteface.HttpServlet;

public class MyThread5 implements Runnable {

    private HttpServlet httpServlet;
    private Response response;
    private Request request;

    public MyThread5(HttpServlet httpServlet, Response response, Request request) {
        this.httpServlet = httpServlet;
        this.response = response;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            httpServlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
