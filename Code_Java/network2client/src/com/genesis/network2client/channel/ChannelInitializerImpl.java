package com.genesis.network2client.channel;

import com.genesis.network2client.floodfilter.FloodByteAttackFilter;
import com.genesis.network2client.floodfilter.FloodCmdAttackFilter;
import com.genesis.network2client.floodfilter.FloodFilterService;
import com.genesis.core.net.msg.SCMessage;
import com.genesis.network2client.codec.ServerToClientMessageEncoder;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 通道初始化方法的实现类
 *
 * <p>因为添加了防洪水攻击的策略，所以适用于与client之间的通信
 * @author Joey
 *
 */
public class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

    final private MessageToByteEncoder<SCMessage> encoder = new ServerToClientMessageEncoder();
    final private ChannelInboundHandlerAdapter handler;
    final private Class<? extends ByteToMessageDecoder> classOfDecoder;

    public ChannelInitializerImpl(ChannelInboundHandlerAdapter handler,
            Class<? extends ByteToMessageDecoder> classOfDecoder) {
        this.handler = handler;
        this.classOfDecoder = classOfDecoder;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final String ip = ch.remoteAddress().getHostString();
        if (FloodFilterService.Inst.isForbidden(ip)) {
            ch.disconnect();
        }

        FloodByteAttackFilter byteFilter = new FloodByteAttackFilter(ip);
        FloodCmdAttackFilter cmdFilter = new FloodCmdAttackFilter(ip);

        ch.pipeline().addLast("byteFilter", byteFilter)
                .addLast("decoder", classOfDecoder.newInstance()).addLast("cmdFilter", cmdFilter)
                .addLast("encoder", encoder).addLast("Handler", handler);
    }

}
