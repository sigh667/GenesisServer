package com.genesis.core.isc.session;

import com.google.protobuf.GeneratedMessage;

import com.genesis.core.isc.msg.IMessage;
import com.genesis.core.isc.msg.ToPlayerMessage;
import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.net.msg.SCMessage;
import com.genesis.protobuf.MessageType;

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
