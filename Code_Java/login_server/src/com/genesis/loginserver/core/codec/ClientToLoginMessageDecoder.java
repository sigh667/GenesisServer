package com.genesis.loginserver.core.codec;

import com.mokylin.bleach.core.net.msg.CSMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * 客户端发送给Login服务器的消息解析器。<p>
 * 每个客户端连接，都拥有一个这样的Decoder对象
 * <p>
 * 该解析器所解析的消息协议定义如下：<br>
 * +---------------------+------+-------+--------------+ <br>
 * | message body length | type | index | message body | <br>
 * +---------------------+------+-------+--------------+ <br>
 * <br>
 * 1. 消息体的长度: 2 byte, 用于解析时从byte流中剥离消息;<br>
 * 2. 消息号：2 byte, 用于指定具体的消息类型;<br>
 * 3. 序列号：1 byte，无符号，每次发包都+1，首次的值暂由客户端随机;<br>
 * 4. 消息体：长度由1指定, 具体的消息内容;<br>
 *
 * @author Joey
 */
public class ClientToLoginMessageDecoder extends ByteToMessageDecoder {

    private static Logger log = LoggerFactory.getLogger(ClientToLoginMessageDecoder.class);

    /**
     * 是否初始化（以收到第一个消息为初始化标志）
     */
    private boolean isInited = false;
    /**
     * 序号（存放客户端上次发来的值）
     */
    private byte index = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        in.markReaderIndex();

        // 0.1 检查消息头
        int readableBytes = in.readableBytes();
        if (readableBytes < 5) {
            // 没有足够的数据用于解析
            log.debug("readableBytes==" + readableBytes);
            return;
        }


        //这里是正确的，不用循环读取，因为超类里已经做了
        ByteBuf littleEdianIn = in.order(ByteOrder.LITTLE_ENDIAN);
        final int msgBodyLength = littleEdianIn.readUnsignedShort();
        final int msgType = littleEdianIn.readUnsignedShort();
        final byte indexTmp = littleEdianIn.readByte();

        // 0.2 检查消息体是否到达
        if (littleEdianIn.readableBytes() < msgBodyLength) {
            //全部消息没有完全到达，停止本次解析，等待全部到达后再解析
            in.resetReaderIndex();
            return;
        }

        // 1.0 验证序号(目的：防止消息重放)
        if (!isInited) {
            index = indexTmp;
            isInited = true;
        } else {
            if (indexTmp - 1 == index) {
                // 验证通过
                index += 1;
            } else {
                // 序号错误，直接踢掉
                ctx.disconnect();
                return;
            }
        }

        // 2.0 发给逻辑层
        CSMessage csMsg = new CSMessage(msgType, littleEdianIn.readBytes(msgBodyLength).array());
        out.add(csMsg);
    }

}
