package com.genesis.servermsg.core.isc.session;

import com.genesis.core.net.msg.SCMessage;
import com.genesis.protobuf.MessageType;
import com.genesis.servermsg.core.isc.msg.ToPlayerMessage;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.servermsg.core.msg.IMessage;
import com.google.protobuf.GeneratedMessage;

public class PlayerSession implements ISession {

    private IRemote remote;
    private long id;

    public PlayerSession(IRemote remote, long id) {
        this.remote = remote;
        this.id = id;
    }

    @Override
    public void sendMessage(GeneratedMessage msg) {
        SCMessage scmsg = new SCMessage(
                msg.getDescriptorForType().getOptions().getExtension(MessageType.gcMessageType)
                        .getNumber(), msg.toByteArray());
        ToPlayerMessage message = new ToPlayerMessage(id, scmsg);
        this.remote.sendMessage(message);
    }

    @Override
    public <T extends GeneratedMessage.Builder<T>> void sendMessage(
            GeneratedMessage.Builder<T> msg) {
        SCMessage scmsg = new SCMessage(
                msg.getDescriptorForType().getOptions().getExtension(MessageType.gcMessageType)
                        .getNumber(), msg.build().toByteArray());
        ToPlayerMessage message = new ToPlayerMessage(id, scmsg);
        this.remote.sendMessage(message);
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void sendMessage(IMessage msg) {
        this.remote.sendMessage(msg);
    }
}
