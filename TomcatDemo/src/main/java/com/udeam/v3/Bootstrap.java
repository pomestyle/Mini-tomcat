package com.udeam.v3;

import com.udeam.util.HttpUtil;
import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;
import com.udeam.v3.inteface.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动类入库
 * 用于启动tomcat
 * <p>
 * 版本三:实现servlet规范,定义servlet 实现动态请求
 */
public class Bootstrap {


    private static final Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 监听端口号
     * 用于启动socket监听的端口号
     */
    private int port = 8080;


    /**
     * 启动方法
     */
    public void start() throws IOException {

        ServerSocket socket = new ServerSocket(port);
        System.out.println("--------- start port : " + port);

        while (true) {
            Socket accept = socket.accept();
            //获取输入流
            InputStream inputStream = accept.getInputStream();
            //封装请求和响应对象
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());
            if (!servletMap.containsKey(request.getUrl())) {
                response.outPutStr(HttpUtil.resp_200(request.getUrl() + " is not found ... "));
            } else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                try {
                    //处理请求
                    httpServlet.service(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-name>test</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();

                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /test
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.loadServlet();
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
