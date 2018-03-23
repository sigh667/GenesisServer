package com.genesis.servermsg.core.isc.remote.actorrefs;

import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.core.isc.ServerType;

/**
 * 代表组装好的的ActorRef的接口。
 *
 * @author pangchong
 *
 */
public interface IActorPackages {

    public void sendMessage(Object sendingMsg, MessageTarget target);

    public ServerType getServerType();

    public int getServerId();
}
