package com.genesis.core.isc.remote.actorrefs;

import com.genesis.core.isc.ServerType;
import com.genesis.protobuf.MessageType.MessageTarget;

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
