package com.genesis.test.gameserver.net.withclient;

import com.google.protobuf.InvalidProtocolBufferException;

import com.genesis.core.net.msg.CSMessage;
import com.genesis.core.net.msg.SCMessage;
import com.genesis.test.gameserver.net.MockCGMessage.CGLogin;
import com.genesis.test.gameserver.net.MockCGMessage.GCLogin;
import com.genesis.test.gameserver.net.MockCGMessage.Player;
import com.genesis.test.protobuf.MockMessageType;
import com.genesis.network2client.handle.INettyMessageHandler;
import com.genesis.network2client.session.IClientSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockLoginMessageHandler implements INettyMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MockLoginMessageHandler.class);

    @Override
    public void handle(IClientSession session, CSMessage msg) {
        if (msg.messageContent != null) {
            CGLogin login = null;

            try {
                login = CGLogin.PARSER.parseFrom(msg.messageContent);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            log(login);
        }

        GCLogin.Builder gc = GCLogin.newBuilder();
        gc.addPlayers(Player.newBuilder().setHuman("宝亮"));
        gc.addPlayers(Player.newBuilder().setHuman("浩老板"));
        gc.addPlayers(Player.newBuilder().setHuman("くろさきいちご"));
        gc.setId(Long.MAX_VALUE);
        gc.setName("黒崎一護");
        gc.setPass(-98712223);
        gc.setMoney(Float.MIN_NORMAL);
        gc.setExp(Double.MAX_VALUE);


        session.sendMessage(new SCMessage(MockMessageType.GCMessageType.GC_LOGIN_VALUE,
                gc.build().toByteArray()));
    }

    private void log(CGLogin login) {
        if (login == null) {
            return;
        }

        log.error("ID: {}", login.getId());
        log.error("Name: {}", login.getName());
        log.error("Pass: {}", login.getPass());
        log.error("Ratio: {}", login.getRatio());
        log.error("Player count: {}", login.getPlayersCount());
        if (login.getPlayersCount() > 0) {
            for (Player player : login.getPlayersList()) {
                log.error("Player name: {}", player.getHuman());
            }
        }
    }

}
