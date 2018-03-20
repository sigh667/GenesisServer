package com.genesis.servermsg.gameserver.server;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

import akka.actor.ActorRef;

public class StartNewServer implements IMessage {

    public final ServerConfig newServerConfig;

    public StartNewServer(ServerConfig config) {
        this.newServerConfig = config;
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.SERVER_MANAGER;
    }

    public static class Failed {
        public final String msg;

        public Failed(String msg) {
            this.msg = msg;
        }
    }

    public static class Succeed {
        public final ActorRef newServer;

        public Succeed(ActorRef newServer) {
            this.newServer = newServer;
        }
    }
}
