package com.udeam.util;

/**
 * 封装http响应
 */
public class HttpUtil {

    private static final String content = "<H2>404 page... </H2>";

    /**
     * 添加响应头信息
     * <p>
     * http响应体格式
     * <p>
     * 响应头(多参数空格换行)
     * 换行
     * 响应体
     */
    public static String addHeadParam(int len) {
        String head = "HTTP/1.1 200 OK \n";
        head += "Content-Type: text/html; charset=UTF-8 \n";
        head += "Content-Length: " + len + " \n" + "\r\n";
        return head;
    }

    /**
     * 4040响应
     *
     * @return
     */
    public static String resp_404() {
        String head = "HTTP/1.1 404 not  found \n";
        head += "Content-Type: text/html; charset=UTF-8 \n";
        head += "Content-Length: " + content.length() + " \n" + "\r\n";
        return head + content;
    }

    /**
     * 200响应
     *
     * @param content 响应内容
     * @return
     */
    public static String resp_200(String content) {
        String resul = addHeadParam(content.length()) + content;
        return resul;
    }

}
