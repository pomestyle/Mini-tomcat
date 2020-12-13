package com.udeam.v3.inteface;

import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;

/**
 * 实现servlet规范
 */
public abstract class HttpServlet implements Servlet {

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);


    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod()
        )) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }
}
