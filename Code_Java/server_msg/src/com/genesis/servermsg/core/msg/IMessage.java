package com.genesis.servermsg.core.msg;

import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 服务器间通信消息的接口。<p>
 *
 * 服务器间通信消息推荐实现该接口，实现了该接口的消息可以
 * 通过{@link IRemote}直接发送消息到远程。
 *
 * @author pangchong
 *
 */
public interface IMessage {

    public MessageTarget getTarget();
}
