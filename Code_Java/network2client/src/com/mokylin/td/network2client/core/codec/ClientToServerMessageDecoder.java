package com.mokylin.td.network2client.core.codec;

import com.mokylin.td.network2client.core.msg.ClientMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * 客户端发给服务器的消息的解析器
 *
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
 * <p>2018-03-06 19:40
 *
 * @author Joey
 **/
public class ClientToServerMessageDecoder extends ByteToMessageDecoder {

    private static Logger log = LoggerFactory.getLogger(ClientToServerMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        // 1.1 检查消息头
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

        // 1.2 检查消息体是否到达
        if (littleEdianIn.readableBytes() < msgBodyLength) {
            //全部消息没有完全到达，停止本次解析，等待全部到达后再解析
            in.resetReaderIndex();
            return;
        }

        // 2.0 发给逻辑层
        ClientMsg cMsg = new ClientMsg(msgType, littleEdianIn.readBytes(msgBodyLength).array(), indexTmp);
        out.add(cMsg);
    }
}
