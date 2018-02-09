package com.mokylin.td.loginserver.handlers;

import com.icewind.protobuf.LoginMessage;
import com.mokylin.td.loginserver.core.process.IClientMsgHandler;
import com.mokylin.td.loginserver.core.version.Version;
import com.mokylin.td.network2client.core.session.IClientSession;

/**
 * @description: 客户端登陆
 * @author: Joey
 * @create: 2018-02-08 18:57
 **/
public class CGLoginHandler implements IClientMsgHandler<LoginMessage.CSLogin> {


    @Override
    public void handle(IClientSession session, LoginMessage.CSLogin cgLogin) {
        final String accountId = cgLogin.getAccountId();
        final String channel = cgLogin.getChannel();
        final String key = cgLogin.getKey();
        final String macAddress = cgLogin.getMacAddress();
        final String version = cgLogin.getVersion();

        //1.0 开始验证
        //1.1 检验版本
        if (!Version.Inst.isAllowed(version)) {
            notifyFail(session, LoginMessage.LoginFailReason.VERSION_NOT_ALLOW);
            session.disconnect();
            return;
        }

        //1.2 如果是封测，检验账号是否激活

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

//    @Override
//    public void handle(IClientSession session, CS_Login msg) {
//        // TODO 终于可以写登陆的逻辑
//        final int serverId = msg.getserverId();//服务器ID
//
//        //1.0 对此server进行验证
//        //1.1验证该serverId是不是本服负责的
//        final WorldInfo worldInfo = WorldMng.getWorldInfo(serverId);
//        if (worldInfo == null) {
//            session.disconnect();
//            return;
//        }
//        //1.2该服务器是否允许登陆
//        final WorldStatus worldStatus = worldInfo.getWorldStatus();
//        if (worldStatus != WorldStatus.Ok) {
//            // 通知客户端，服务器尚未开放 TODO
//            session.disconnect();
//            return;
//        }
//        //1.3该服务器是否人数已满
//        if (worldInfo.isFull()) {
//            // 通知客户端，服务器人满 TODO
//            // 日后如果有加排队功能的需求，就在这里改
//            session.disconnect();
//            return;
//        }
//
//        final String channel = msg.getchannel();// 渠道ID
//        final String account = msg.getaccount();// 账号
//        final String key = msg.getkey();
//        if (Globals.getServerConfig().isLocalAuth()) {
//            // 本地认证
//            AuthLocal(session, channel, account, key);
//        } else {
//            // 平台认证
//            AuthPlatform(session, channel, account, key);
//        }
//    }

    /**
     * 本地认证
     * @param session
     * @param channel
     * @param account
     * @param key 必须为"",才允许登陆
     */
    private void AuthLocal(IClientSession session, String channel, String account, String key) {
        // 这是为了在外网登录墙开启的情况下，强行登录GM账号进入游戏而做的
        if (key != null) {
            // 通知客户端，服务器尚未开放 TODO
            session.disconnect();
            return;
        }

        // 1.0 在缓存中查询，此账号是否存在
        //		final IRedis iRedis = Globals.getiRedis();
        //		//<服，渠道+账号，Account>
        //		iRedis.getHashOp().hsetnx(key, field, value)
    }

    /**
     * 平台认证
     * @param session
     * @param channel
     * @param account
     * @param getkey
     */
    private void AuthPlatform(IClientSession session, String channel, String account,
            String getkey) {
        // TODO Auto-generated method stub
    }
}
