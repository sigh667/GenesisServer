package com.genesis.loginserver.core;

import com.genesis.network2client.session.IClientSession;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.annotation.NotThreadSafe;

/**
 * 在线玩家容器
 * <p>只可以在单线程中使用
 * <p>2018-03-15 21:03
 *
 * @author Joey
 **/
@NotThreadSafe
public class OnlinePlayerContainer {
    // <渠道，账号ID，Session>
    private static Table<String, String, IClientSession> sessionsLogined = HashBasedTable.create();
    // 常量：无效线程ID
    private static final long INVALID_THREAD_ID = -1;
    // 线程ID
    private static long threadId = INVALID_THREAD_ID;

    /**
     * 登入
     * @param channel
     * @param accountId
     */
    public static void onLogin(String channel, String accountId, IClientSession session) {
        checkThread();
        sessionsLogined.put(channel,accountId, session);
    }

    /**
     * 是否在线
     * @param channel
     * @param accountId
     * @return
     */
    public static boolean isOnline(String channel, String accountId) {
        checkThread();
        return sessionsLogined.contains(channel, accountId);
    }

    /**
     * 登出
     * @param channel
     * @param accountId
     */
    public static void onLogout(String channel, String accountId) {
        checkThread();
        sessionsLogined.remove(channel, accountId);
    }


    /**
     * 检查线程
     */
    private static void checkThread() {
        if (threadId== INVALID_THREAD_ID) {
            threadId = Thread.currentThread().getId();
        } else {
            final long tmpId = Thread.currentThread().getId();
            if (tmpId!=threadId)
                throw new RuntimeException("OnlinePlayerContainer 禁止在多个线程访问！曾经访问的线程ID==" + threadId
                        + ", 当前线程ID" + tmpId);
        }
    }
}
