package com.udeam.v5.bean;


/**
 * mapper组装体系接口
 * <p>
 * 这儿先配置一Host请求 即： 一个host下 多个项目，一个项目下多个url 然后对应多个servlet
 */
public class MapperContext {

    /**
     * 虚拟主机
     */
    private Host host;

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
