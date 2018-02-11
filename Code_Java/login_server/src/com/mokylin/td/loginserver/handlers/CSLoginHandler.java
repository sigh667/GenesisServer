package com.mokylin.td.loginserver.handlers;

import com.icewind.protobuf.LoginMessage;
import com.mokylin.td.loginserver.core.process.IClientMsgHandler;
import com.mokylin.td.loginserver.core.version.Version;
import com.mokylin.td.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * 客户端登陆
 * <p>2018-02-08 18:57
 * @author Joey
 **/
public class CSLoginHandler implements IClientMsgHandler<LoginMessage.CSLogin> {


    @Override
    public void handle(IClientSession session, LoginMessage.CSLogin csLogin) {
        final String accountId = csLogin.getAccountId();
        final String channel = csLogin.getChannel();
        final String key = csLogin.getKey();
        final String macAddress = csLogin.getMacAddress();
        final String version = csLogin.getVersion();

        //0.0 检验版本
        if (!Version.Inst.isAllowed(version)) {
            // 无论是谁，版本不一致，都不允许登陆
            notifyFail(session, LoginMessage.LoginFailReason.VERSION_NOT_ALLOW);
            session.disconnect();
            return;
        }

        //0.1 检测该账号是否是GM账号
        if (isGM(channel, accountId)) {
            // GM有特权，可以直接到认证这一步
            auth(session, channel, accountId, key);
            return;
        }

        //1.0 开始验证
        //1.1 检查服务器是否开放
        if (!Globals.isIsServerOpen()) {
            session.sendMessage(LoginMessage.SCLoginServerNotOpen.getDefaultInstance());
            session.disconnect();
            return;
        }
        //1.2 检查整个大区是否人满 TODO

        //1.3 检查本服务器是否人满（如果满了而其他LoginServer有余量，则给玩家指定另一个LoginServer） TODO

        //1.4 如果是封测，检验账号是否激活 TODO

        //2.0 认证
        auth(session, channel, accountId, key);
    }

    /**
     * 登陆认证及后续逻辑
     * @param channel
     * @param accountId
     * @param key
     */
    private void auth(IClientSession session, String channel, String accountId, String key) {
        // 1.认证
        if (Globals.getServerConfig().isLocalAuth()) {
            // 本地认证
            if (!authLocal(session, channel, accountId, key))
                return;
        } else {
            // 平台认证
            if (!authPlatform(session, channel, accountId, key))
                return;
        }

        // 2.如果有必要则，进入排队列表 TODO

        // 3.分配一个Gate给此玩家 TODO

    }

    /**
     * 是否为GM账号（此处只查询账号属性，不做账号认证）
     * @param channel
     * @param accountId
     * @return
     */
    private boolean isGM(String channel, String accountId) {
        // 去LoginRedis中查询，该账号是否为GM账号 TODO
        return false;
    }

    /**
     * 通知客户端，登陆失败
     * @param session   客户端连接
     * @param reason    失败原因
     */
    private void notifyFail(IClientSession session, LoginMessage.LoginFailReason reason) {
        final LoginMessage.SCLoginFail.Builder builder = LoginMessage.SCLoginFail.newBuilder();
        builder.setFailReason(reason);
        session.sendMessage(builder);
    }

    /**
     * 本地认证
     * @param session   客户端连接
     * @param channel   渠道
     * @param account   账号ID
     * @param key       验证字串(本地认证模式下，必须为"",才允许登陆)
     * @return  是否认证通过
     */
    private boolean authLocal(IClientSession session, String channel, String account, String key) {
        // 这是为了在外网登录墙开启的情况下，强行登录GM账号进入游戏而做的
        if (key != null) {
            // 通知客户端，服务器尚未开放 TODO
            session.disconnect();
            return false;
        }

        // 1.0 在缓存中查询，此账号是否存在
        //		final IRedis iRedis = Globals.getiRedis();
        //		//<服，渠道+账号，Account>
        //		iRedis.getHashOp().hsetnx(key, field, value)

        return true;
    }

    /**
     * 平台认证
     * @param session   客户端连接
     * @param channel   渠道
     * @param account   账号ID
     * @param key       验证字串
     * @return  是否认证通过
     */
    private boolean authPlatform(IClientSession session, String channel, String account, String key) {
        // TODO Auto-generated method stub
        return false;
    }
}
