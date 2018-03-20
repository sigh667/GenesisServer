package com.genesis.logindbserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 为登陆服务的DBS
 * <p>2018-03-17 14:45
 *
 * @author Joey
 **/
public class LoginDBServer {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(LoginDBServer.class);

    public static void main(String[] args) {

        try {

        } catch (Exception e) {
            logger.error("LoginDBServer 启动失败！！！", e);
            System.exit(-1);
        }

        logger.info(
                "\n\n==========================LoginDBServer startup successful！==========================\n\n");
    }
}
