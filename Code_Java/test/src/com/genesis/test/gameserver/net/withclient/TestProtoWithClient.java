package com.genesis.test.gameserver.net.withclient;

import com.genesis.test.gameserver.net.RecordSessionImpl;
import com.genesis.network2client.channel.IChannelListener;

public class TestProtoWithClient {

    public static void main(String[] s) {
        final MockLoginMessageHandler handler = new MockLoginMessageHandler();
        final IChannelListener rs = new RecordSessionImpl();

        try {
            //			NettyNetworkLayer.configNet("168.168.9.80", 12306).start(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
