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
//public class MyServlet1 extends HttpServlet {
//
//    @Override
//    public void init() throws Exception {
//    }
//
//    @Override
//    public void doGet(Request request, Response response) {
//
//        //动态业务请求
//        String contents = "<h2> GET 外部部署MyServlet1</h2>";
//        System.out.println(contents);
//        try {
//            response.outPutStr(HttpUtil.resp_200(contents));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void doPost(Request request, Response response) {
//        //动态业务请求
//        String contents = "<h2> Post 外部部署MyServlet1</h2>";
//        try {
//            response.outPutStr(HttpUtil.resp_200(contents));
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
