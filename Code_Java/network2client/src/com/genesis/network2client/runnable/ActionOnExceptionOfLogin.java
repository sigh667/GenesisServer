package com.genesis.network2client.runnable;

import com.genesis.core.concurrent.fixthreadpool.IActionOnException;
import com.genesis.core.concurrent.fixthreadpool.IRunnableBindId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理登陆消息的过程中发生异常，处理方案：踢掉此客户端
 * @author Joey
 *
 */
public class ActionOnExceptionOfLogin implements IActionOnException {

    private static Logger log = LoggerFactory.getLogger(ActionOnExceptionOfLogin.class);

    @Override
    public void action(IRunnableBindId iRunnableBindId) {
        if (iRunnableBindId instanceof IRunnableWithClientSession) {
            IRunnableWithClientSession runnable = (IRunnableWithClientSession) iRunnableBindId;
            runnable.getSession().disconnect();
        } else if (iRunnableBindId!=null) {
            log.warn("Unknow IRunnableBindId class: " + iRunnableBindId.getClass().getName());
        } else {
            log.warn("iRunnableBindId = null !");
        }
    }

}
