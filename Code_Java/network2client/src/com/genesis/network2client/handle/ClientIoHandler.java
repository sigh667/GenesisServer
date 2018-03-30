package com.genesis.network2client.handle;

import com.genesis.core.net.msg.SCMessage;
import com.genesis.core.time.TimeService;
import com.genesis.core.util.RandomUtil;
import com.genesis.network2client.channel.IChannelListener;
import com.genesis.network2client.id.IdGenerator;
import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.session.IClientSession;
import com.genesis.protobuf.LoginMessage;
import com.genesis.protobuf.MessageType;
import com.google.protobuf.GeneratedMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器响应客户端IO消息的Handler，用于netty。<p>
 *
 * 该handler主要响应的事件：<br>
 * <li>channelActive: 连接建立，增加ctx个数，将对应的ChannelHandlerContext包装成ISession</li>
 * <li>channelInactive: 连接关闭，减少ctx个数，移除对应的上下文ISession</li>
 * <li>channelRead: 收取消息，将合法的消息交由INettyMessageHandler处理</li>
 * <li>exceptionCaught: 捕获异常，记录日志，断开客户端连接</li>
 * <br>
 *
 * 该handler在创建的时候，可以使用默认提供的MessageProcess作为消息处理器，也可以根据需要指定自己的实现。
 *
 * @see IClientSession
 * @see IClientMessageHandler
 *
 * @author Joey
 *
 */

@Sharable
public class ClientIoHandler extends ChannelInboundHandlerAdapter {
    /** 日志 */
    private final static Logger log = LoggerFactory.getLogger(ClientIoHandler.class);

    /** 会话对应的键 */
    public final static AttributeKey<IClientSession> sessionKey =
            AttributeKey.valueOf("SessionKey");
    /** 网络消息处理器 */
    private final IClientMessageHandler msgProcess;
    /** 网络通道监听器 */
    private final IChannelListener channelListener;
    /** 连接的个数 */
    private AtomicInteger ctxCount = new AtomicInteger(0);

    public ClientIoHandler(IClientMessageHandler msgProcess, IChannelListener channelListener) {
        if (msgProcess == null) {
            throw new IllegalArgumentException("Message Process for IO Handler can not be null!");
        }
        this.msgProcess = msgProcess;
        if (channelListener == null) {
            throw new IllegalArgumentException("Record Session for IO Handler can not be null!");
        }
        this.channelListener = channelListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctxCount.incrementAndGet();
        log.info("Session opened: {}", ctx.channel().localAddress());
        IClientSession session = new GameSession(ctx);
        this.channelListener.onChannelActive(session);
        ctx.attr(sessionKey).set(session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctxCount.decrementAndGet();
        this.channelListener.onChannelInActive(ctx.attr(sessionKey).get());
        ctx.attr(sessionKey).remove();
        log.info("Session closed: {}", ctx.channel().localAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ClientMsg)) {
            log.warn("Something received strange: {}", msg);
            return;
        }

        ClientMsg cMsg = (ClientMsg) msg;
        IClientSession session = ctx.attr(sessionKey).get();
        if (log.isDebugEnabled()) {
            log.debug("[[[Received message]]]: session - {}, msgType - {}",
                    session.getSessionId(), cMsg.messageType);
        }

        if (session.isIndexGenerated()) {
            final byte index = session.incIndexAndGet();
            if (index != cMsg.index) {
                session.disconnect();
                return;
            }
        } else if (MessageType.CGMessageType.CS_LOGIN_HANDSHAKE.getNumber() == cMsg.messageType) {
            // 如果是握手消息，直接在这里回复
            LoginMessage.SCHandshakeReply.Builder builder = LoginMessage.SCHandshakeReply.newBuilder();
            final byte index = session.generateIndex();
            builder.setIndexBegin(index);
            session.sendMessage(builder);
            return;
        } else {
            // 序号未初始化，不接收其他消息
            session.disconnect();
            return;
        }

        msgProcess.handle(session, cMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.disconnect();
    }

    /**
     * ChannelHandlerContext包装类，实现了IClientSession，为之后的网络操作提供操作。
     *
     * @see IClientSession
     *
     * @author Joey
     *
     */
    static class GameSession implements IClientSession {

        // session存储的数据
        /**由所在服务器生成，自增的（由于long足够大，哪怕每秒1000个连接，也可以连续坚持几亿年，所以就不考虑循环使用的问题了）*/
        private final long sessionId;
        private final ChannelHandlerContext ctx;
        private int targetGameServerId;
        private volatile long uuid;
        /** 建立连接时的时间*/
        private long connectedTime;

        /**是否初始化（收到握手消息时，进行初始化）*/
        private boolean isGenerateed = false;
        /**序号（存放客户端上次发来的值）*/
        private byte index = 0;

        /**渠道*/
        private String channel;
        /**账号*/
        private String accountId;

        GameSession(ChannelHandlerContext ctx) {
            this.ctx = ctx;
            sessionId = IdGenerator.getNextId();
            connectedTime = TimeService.Inst.now();
        }

        @Override
        public void sendMessage(SCMessage msg) {
            if (log.isDebugEnabled()) {
                log.debug("[[[Send message]]]: session - {}, code - {}, type - {}", getSessionId(),
                        msg.messageType, MessageType.GCMessageType.valueOf(msg.messageType));
            }
            ctx.writeAndFlush(msg);
        }

        @Override
        public void sendMessage(GeneratedMessage msg) {
            SCMessage scmsg = new SCMessage(
                    msg.getDescriptorForType().getOptions().getExtension(MessageType.gcMessageType)
                            .getNumber(), msg.toByteArray());
            sendMessage(scmsg);
        }

        @Override
        public <T extends GeneratedMessage.Builder<T>> void sendMessage(GeneratedMessage.Builder<T> msg) {
            sendMessage((GeneratedMessage) msg.build());
        }

        @Override
        public void disconnect() {
            ctx.disconnect();
        }

        @Override
        public long getSessionId() {
            return sessionId;
        }

        @Override
        public void setChannel(String channel) {
            this.channel = channel;
        }

        @Override
        public String getChannel() {
            return channel;
        }

        @Override
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        @Override
        public String getAccountId() {
            return accountId;
        }

        @Override
        public int getTargetGameServerId() {
            return targetGameServerId;
        }

        @Override
        public void setTargetGameServerId(int gameServerId) {
            this.targetGameServerId = gameServerId;
        }

        @Override
        public long getUuid() {
            return this.uuid;
        }

        @Override
        public void setUuid(long uuid) {
            this.uuid = uuid;
        }

        @Override
        public long getConnectedTime() {
            return connectedTime;
        }

        @Override
        public boolean isInActive() {
            return !ctx.channel().isActive();
        }

        @Override
        public String getClientAddress() {
            return ctx.channel().localAddress().toString();
        }

        @Override
        public boolean isIndexGenerated() { return isGenerateed;}
        @Override
        public byte generateIndex() {
            index = (byte) RandomUtil.nextInt(128);
            isGenerateed = true;
            return index;
        }
        @Override
        public byte incIndexAndGet() { return ++index; }
    }
}

