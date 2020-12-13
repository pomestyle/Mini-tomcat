package com.udeam.v3.inteface;

import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;

/**
 * 自定义servlet规范
 */
public interface Servlet {


    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
