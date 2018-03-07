package com.genesis.gateserver.core.net.channel;

import com.mokylin.bleach.core.annotation.NotThreadSafe;
import com.mokylin.td.network2client.core.channel.IChannelListener;
import com.mokylin.td.network2client.core.session.ClientSessionContainer;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * 网关的netty通道监听器
 *
 * <p>此类负责生成Session的临时唯一Id，以供玩家登陆成功之后可以找到该玩家对应的Session<br>
 * 还负责通知网关负载均衡服务器玩家在该网关的登陆和退出
 *
 * @author yaguang.xiao
 *
 */

@NotThreadSafe
public class AgentServerChannelListener implements IChannelListener {

    @Override
    public void onChannelActive(IClientSession session) {
        ClientSessionContainer.Inst.insert(session);
    }

    @Override
    public void onChannelInActive(IClientSession session) {
        ClientSessionContainer.Inst.remove(session.getSessionId());

        //通知其所登录的GameServer，某玩家登出 TODO

    }

}
