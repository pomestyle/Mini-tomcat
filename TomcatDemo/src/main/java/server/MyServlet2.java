//package server;
//
//import com.udeam.util.HttpUtil;
//import com.udeam.v2.bean.Request;
//import com.udeam.v2.bean.Response;
//import com.udeam.v3.inteface.HttpServlet;
//
//import java.io.IOException;
//
///**
// * 业务类servelt
// */
//public class MyServlet2 extends HttpServlet {
//
//    @Override
//    public void init() throws Exception {
//    }
//
//    @Override
//    public void doGet(Request request, Response response) {
//
//
//        //动态业务请求
//        String cc = "<h3> GET 外部部署MyServlet2</h3>";
//
//        System.out.println(cc);
//        try {
//            response.outPutStr(HttpUtil.resp_200(cc));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void doPost(Request request, Response response) {
//        //动态业务请求
//        String content = "<h2> Post 外部部署MyServlet2</h2>";
//        try {
//            response.outPutStr(HttpUtil.resp_200(content));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void destory() throws Exception {
//
//    }
//}
