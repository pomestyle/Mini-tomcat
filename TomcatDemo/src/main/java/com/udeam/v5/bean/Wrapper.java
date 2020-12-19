package com.udeam.v5.bean;

/**
 * 请求url 用来锁定请求servlet
 */
public class Wrapper {
    private String url;

    /**
     * url对应的servlet实例
     */
    private Object object;

    /**
     * web.xml里面配置的全限定名
     */
    private String servletClass;

    public String getUrl() {
        return url;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }
}
