package com.genesis.redis.login;

import java.util.ArrayList;

/**
 * 即将登陆的Client信息
 * <p>2018-02-24 18:07
 *
 * @author Joey
 **/
public class LoginClientInfo {
    /**账号ID*/
    public String accountId;
    /**渠道*/
    public String channel;
    /**本次登陆使用的验证码，用后即失效*/
    public ArrayList<Integer> vCode;
}
