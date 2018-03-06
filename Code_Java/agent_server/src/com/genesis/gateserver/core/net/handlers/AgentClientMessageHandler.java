package com.genesis.gateserver.core.net.handlers;

import com.genesis.gateserver.core.frontend.gameserver.GameServerFrontend;
import com.genesis.gateserver.core.global.Globals;
import com.genesis.gateserver.core.msgtarget.MessageTargetManager;
import com.genesis.gateserver.core.session.AgentClientSessions;
import com.genesis.protobuf.MessageType;
import com.genesis.protobuf.agentserver.AgentMessage.CGGameServerInfo;
import com.google.common.base.Optional;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mokylin.bleach.core.net.msg.CSSMessage;
import com.mokylin.bleach.servermsg.gameserver.PlayerConnected;
import com.mokylin.td.network2client.core.handle.IClientMessageHandler;
import com.mokylin.td.network2client.core.msg.ClientMsg;
import com.mokylin.td.network2client.core.session.IClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关的客户端消息处理器
 *
 * <p>
 * 这里面的{@link #handle(IClientSession, ClientMsg)}方法在netty的线程中执行<br>
 * 在netty中选用的线程策略是来自同一个客户端的消息必然在相同的线程中处理，来自不同客户端的消息不一定在相同的线程中处理
 *
 * @author Joey
 *
 */
public class AgentClientMessageHandler implements IClientMessageHandler {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AgentClientMessageHandler.class);


    @Override
    public void handle(IClientSession session, ClientMsg msg) {

        if (msg.messageType == MessageType.CGMessageType.CG_GAME_SERVER_INFO_VALUE) {
            CGGameServerInfo gsInfo = null;
            try {
                gsInfo = CGGameServerInfo.PARSER.parseFrom(msg.messageContent);
            } catch (InvalidProtocolBufferException e) {
                logger.error("CGGameServerInfo parse fail!", e);
            }
            if (gsInfo == null) {
                return;
            }

            int gsId = gsInfo.getServerId();
            Optional<GameServerFrontend> gsOption =
                    Globals.getGameServerManager().getGameServerFrontend(gsId);

            if (gsOption.isPresent()) {
                final long id = session.getSessionId();
                AgentClientSessions.Inst.put(id, session);

                session.setTargetGameServerId(gsInfo.getServerId());
                //TODO 这里面的有些数据是需要客户端发上来的，现在先填默认值
                gsOption.get().sendMessage(id,
                        new PlayerConnected(id, session.getClientAddress(), "", "", "", "", ""));
            } else {
                logger.warn("Can not find game server [{}].", gsId);
                session.setTargetGameServerId(-1);
                session.disconnect();
                return;
            }

        } else {
            if (session.isInActive()) {
                return;
            }

            long agentSessionId = session.getSessionId();
            CSSMessage cssMsg = new CSSMessage(agentSessionId, msg.messageType, msg.messageContent);
            if (MessageTargetManager.isGameServerMsg(msg.messageType)) {
                final int serverId = session.getTargetGameServerId();
                Optional<GameServerFrontend> option =
                        Globals.getGameServerManager().getGameServerFrontend(serverId);
                if (option.isPresent()) {
                    option.get().route(session, cssMsg);
                }
            } else {
                MessageTargetManager.sendMsgToOtherServer(cssMsg);
            }
        }
    }

}
