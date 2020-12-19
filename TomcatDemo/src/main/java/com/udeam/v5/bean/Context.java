package com.udeam.v5.bean;

import java.util.ArrayList;
import java.util.List;

public class Context {

    /**
     * 请求url 用来锁定servlet
     */
    private List<Wrapper> wrappersList;


    /**
     * context name 项目名 也就是上下文名
     */
    String name;

    public Context(String name) {
        this.name = name;
        wrappersList = new ArrayList<>();
    }
    public Context() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Wrapper> getWrappersList() {
        return wrappersList;
    }

    public void setWrappersList(List<Wrapper> wrappersList) {
        this.wrappersList = wrappersList;
    }
}
