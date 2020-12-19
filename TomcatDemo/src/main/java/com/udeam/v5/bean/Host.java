package com.udeam.v5.bean;

import java.util.ArrayList;
import java.util.List;

public class Host {

    /**
     * 虚拟主机名
     */
    private String hostName;

    /**
     * Context 不同的项目名
     */
    private List<Context> contextList;

    public Host() {
        this.contextList = new ArrayList<>();
    }

    public List<Context> getContextList() {
        return contextList;
    }

    public void setContextList(List<Context> contextList) {
        this.contextList = contextList;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
