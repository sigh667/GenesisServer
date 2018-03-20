package com.genesis.loginserver.config;

import com.genesis.core.config.model.NetInfo;

/**
 * 登陆服务器配置
 * <p>2018-03-05 16:17
 *
 * @author Joey
 **/
public class LoginConfig {

    /**监听客户端的IP端口信息*/
    private NetInfo netInfoToClient;

    /**Excel配置文件所在目录*/
    private String baseResourceDir;
    /**Excel配置文件 是否加密*/
    private boolean isXorLoad;

    /**是否开启本地登陆验证*/
    private boolean localAuth;


    public NetInfo getNetInfoToClient() {
        return netInfoToClient;
    }

    public String getBaseResourceDir() {
        return baseResourceDir;
    }

    public boolean isXorLoad() {
        return isXorLoad;
    }

    public boolean isLocalAuth() {
        return localAuth;
    }
}
