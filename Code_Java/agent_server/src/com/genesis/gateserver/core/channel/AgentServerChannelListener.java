package com.genesis.gateserver.core.channel;

import com.genesis.gateserver.global.Globals;
import com.genesis.network2client.channel.IChannelListener;
import com.genesis.network2client.session.ClientSessionContainer;
import com.genesis.network2client.session.IClientSession;
import com.mokylin.bleach.core.annotation.ThreadSafe;
import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;

/**
 * 网关的netty通道监听器
 *
 * <p>监听ClientSession的开启和关闭
 *
 * @author Joey
 *
 */
@ThreadSafe
public class AgentServerChannelListener implements IChannelListener {

    @Override
    public void onChannelActive(IClientSession session) {
        ClientSessionContainer.Inst.insert(session);
        Globals.onlineClientChange(1);
    }

    @Override
    public void onChannelInActive(IClientSession session) {
        ClientSessionContainer.Inst.remove(session.getSessionId());
        Globals.onlineClientChange(-1);

        //抛消息到逻辑线程
        Globals.getLogicThread().submit(new IRunnableBindId() {
            @Override
            public long bindId() {
                return 0;
            }

            @Override
            public void run() {
                // 如果玩家是在线状态，将其剔除
            }
        });

        //通知其所登录的GameServer，某玩家登出 TODO

    }

}
