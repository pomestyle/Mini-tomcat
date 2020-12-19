package com.udeam.v5;

import com.udeam.util.HttpUtil;
import com.udeam.v2.bean.Request;
import com.udeam.v2.bean.Response;
import com.udeam.v3.inteface.HttpServlet;
import com.udeam.v5.bean.Context;
import com.udeam.v5.bean.Host;
import com.udeam.v5.bean.MapperContext;
import com.udeam.v5.bean.Wrapper;
import com.udeam.v5.config.SunClassloader;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 启动类入库
 * 用于启动tomcat
 * <p>
 * 版本五: 根据webapps下部署的项目，动态根据不同的url处理请求。
 * 使用线程改造 每个请求互相不受影响,不阻塞
 */
public class Bootstrap {

    /**
     * 映射 端口，虚拟主机以及项目，url
     */
    private static final MapperContext mapperContext = new MapperContext();

    /**
     * 存储web项目下的web.xml路径便于之后解析xml
     */
    private static final Map<String, String> DEMO_XML = new HashMap<>();

    /**
     * 存储web项目下web的对象路径
     */
    private static final Map<String, String> DEMO_CLASS = new HashMap<>();
    /**
     * 存储web项目下html的对象路径
     */
    private static final Map<String, String> DEMO_HTML = new HashMap<>();


    /**
     * 端口可以配置在xml里面
     * 监听端口号
     * 用于启动socket监听的端口号
     */
    private int port;

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 参数可以配置在xml里面
     */
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(500));


    /**
     * 启动方法
     */
    public void start() throws IOException {

        //获取上下文
        List<Context> contextList = mapperContext.getHost().getContextList();


        ServerSocket socket = new ServerSocket(port);
        System.out.println("--------- start port : " + port);

        while (true) {
            Socket accept = socket.accept();
            //获取输入流
            InputStream inputStream = accept.getInputStream();
            //封装请求和响应对象
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());

            //请求url
            String url = request.getUrl();
            //获取上下文
            String context = url.substring(0).substring(0, url.substring(1).indexOf("/") + 1);

            //真正请求的url
            String realUrl = url.replace(context, "");
            boolean falg = false;
            //上下文
            Context context1 = null;
            //判断上下文
            for (Context con : contextList) {
                String name = con.getName();
                if (context.equalsIgnoreCase("/" + name)) {
                    falg = true;
                    context1 = con;
                    break;
                }
            }
            if (!falg) {
                response.outPutStr(HttpUtil.resp_404());
                return;
            }

            //获取wrapper  处理请求
            List<Wrapper> wrappersList = context1.getWrappersList();
            for (Wrapper wrapper : wrappersList) {
                //静态资源 html 请求
                if (realUrl.equals(wrapper.getUrl()) && url.endsWith(".html")) {
                    //html 暂时没写，，同servlet一样
                    //剩下的当做servlet请求处理
                } else if (realUrl.equals(wrapper.getUrl())) {
                    HttpServlet httpServlet = (HttpServlet) wrapper.getObject();
                    //1 单线程处理
                    MyThread5 myThread = new MyThread5(httpServlet, response, request);
                    threadPoolExecutor.submit(myThread);
                }
            }

        }

    }

    /**
     * 加载解析web.xml，保存url,Servlet信息
     *
     * @param context    项目上下文
     * @param webXmlPath web.xml路径
     */
    private void loadServlet(String context, String webXmlPath) throws FileNotFoundException {
        //存储url  以及 配置servlet 以及请求url
        List<Wrapper> wrappersList = null;
        //获取上下文
        List<Context> contextList = mapperContext.getHost().getContextList();
        for (Context context1 : contextList) {
            if (context.equals(context1.getName())) {
                wrappersList = context1.getWrappersList();
            }
        }

        //这里读取磁盘位置绝对路径的xml
        InputStream resourceAsStream = new FileInputStream(webXmlPath);


        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-name>server</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();

                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /server
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                //servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

                Wrapper wrapper = new Wrapper();
                wrapper.setServletClass(servletClass);
                wrapper.setUrl(urlPattern);
                //存储servelt信心
                wrappersList.add(wrapper);

            }


        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }


    public void loadServerXml() throws DocumentException {

        //1 加载解析 server.xml文件
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/conf/server.xml");

        SAXReader saxReader = new SAXReader();
        Document read = saxReader.read(resourceAsStream);
        //获取跟路径
        Element rootElement = read.getRootElement();

        Document document = rootElement.getDocument();

        //2 获取端口
        Element node = (Element) document.selectSingleNode("//Connector");
        String port = node.attributeValue("port");
        this.setPort(Integer.valueOf(port));
        //3 获取host
        Element element = (Element) document.selectSingleNode("//Host");
        //虚拟主机
        String localhost = element.attributeValue("name");
        //虚拟主机
        Host host = new Host();
        host.setHostName(localhost);
        mapperContext.setHost(host);


        //部署的地址路径
        String appBase = element.attributeValue("appBase");
        //4 根据这个路径去解析里面的项目 映射端口和虚拟主机，项目，以及url->servlet
        parseAppBase(appBase);


    }


    /**
     * 解析appBase路径下部署的项目，并且将端口，虚拟主机，项目Context,url一一映射封装到MapperContext中
     * 请求的时候可以根据不同的项目以及端口，url响应不同项目的请求
     */
    public void parseAppBase(String path) {
        File file = new File(path);
        //根据路径去加载类
        //1 获取顶级文件名
        File[] files = file.listFiles();

        //设置项目Context
        List<Context> contextList = mapperContext.getHost().getContextList();


        //第一级 路径 也就是文件名 即 项目工程名context
        for (File file1 : files) {
            String name = file1.getName();
            //设置context上下文路径
            contextList.add(new Context(name));
            //递归处理 如果是WEB-INF 和 classes文件则特殊处理
            doFile(file1.getPath(), name);
        }

        System.out.println(DEMO_XML);
        System.out.println(DEMO_CLASS);


        //加载解析web.xml
        doWebXml();
        //实例化class 更新url对应的实例
        doNewInstance();

    }

    /**
     * 读取解析web.xml
     */
    private void doWebXml() {
        for (Map.Entry<String, String> stringStringEntry : DEMO_XML.entrySet()) {

            String context = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();

            try {
                this.loadServlet(context, value);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 类加载实例化
     */
    public static void doNewInstance() {

        //获取上下文集合
        List<Context> contextList1 = mapperContext.getHost().getContextList();
        //所有的上下文
        List<String> contextList = contextList1.stream().map(Context::getName).collect(Collectors.toList());

        //类加载实例化
        for (Map.Entry<String, String> stringStringEntry : DEMO_CLASS.entrySet()) {
            String webDemoName = stringStringEntry.getKey();
            String classPath = stringStringEntry.getValue();


            //加载class 然后实例化
            SunClassloader sunClazz = new SunClassloader();
            try {
                Class<?> clazz = sunClazz.findClass(classPath);
                //根据url查找项目对应的servlet
                if (contextList.contains(webDemoName)) {
                    contextList1.stream().forEach(x -> {
                        if (x.getName().equals(webDemoName)) {
                            List<Wrapper> wrappersList = x.getWrappersList();

                            //判断当前类是否在web.xml配置的servlet class里面
                            wrappersList.stream().forEach(x2 -> {
                                if (classPath.replaceAll("/", ".").contains(x2.getServletClass())) {
                                    //保存实例对象
                                    try {
                                        x2.setObject(clazz.newInstance());
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    });
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 启动入口
     *
     * @param args
     * @throws DocumentException
     */
    public static void main(String[] args) throws DocumentException {
        //启动tomcat
        Bootstrap bootstrap = new Bootstrap();
        try {
            //加载配置server.xml文件
            bootstrap.loadServerXml();
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理web.xml 和 获取字节码
     *
     * @param path
     * @param webDemoName
     */
    static void doFile(String path, String webDemoName) {

        File pathList = new File(path);
        File[] list1 = pathList.listFiles();
        if (list1 == null) {
            return;
        }
        //循环处理每个项目下web.xml
        for (File s : list1) {
            File file1 = new File(s.getPath());
            if (file1.isDirectory()) {
                doFile(file1.getPath(), webDemoName);
            } else {
                if (s.getName().equals("web.xml")) {
                    //保存当前项目下的web.xml
                    DEMO_XML.put(webDemoName, s.getPath());
                }
                //保存字节码路径  这里目前只有一个class文件，其他业务class忽略...
                if (s.getName().endsWith(".class")) {

                    String classPath = s.getPath();
                    DEMO_CLASS.put(webDemoName, classPath);

                }

                //保存html文件
                if (s.getName().endsWith(".html")) {

                    String classPath = s.getPath();
                    DEMO_HTML.put(webDemoName, classPath);

                }
            }
        }

    }

}
