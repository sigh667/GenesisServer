package com.genesis.gameserver.login.funcs;
//
//import HumanInfo;
//import ServerGlobals;
//import HumanInfoCache;
//import AbstractClientMsgFunc;
//import LoginLogger;
//import LoadHumanDataTask;
//import LoginStatus;
//import Player;
//import PlayerManagerArgs;
//import com.genesis.protobuf.PlayerMessage.CGSelectRole;
//
//import java.util.List;
//
///**
// * 处理客户端选择角色消息的函数对象。<p>
// *
// * 该函数对象在PlayerManagerActor中执行。
// *
// * @author pangchong
// *
// */
//public class CGSelectRoleFunc
//        extends AbstractClientMsgFunc<CGSelectRole, ServerGlobals, PlayerManagerArgs> {
//
//    @Override
//    public void handle(Player player, CGSelectRole msg, ServerGlobals sGlobals,
//            PlayerManagerArgs playerManagerArgs) {
//        HumanInfoCache humanInfoCache = playerManagerArgs.humanInfoCache;
//        List<HumanInfo> humans =
//                humanInfoCache.getHumanInfoList(player.getChannel(), player.getAccountId());
//        if (humans == null || humans.isEmpty()) {
//            LoginLogger.log
//                    .warn("逻辑bug，玩家【channel={}】【accountId={}】【agentSessionId={}】选择角色，角色列表却是空的！",
//                            player.getChannel(), player.getAccountId(), player.getId());
//            return;
//        }
//
//        final long roleId = msg.getId();
//        HumanInfo selectHuman = null;
//        for (HumanInfo humanInfo : humans) {
//            if (humanInfo.getId() == roleId) {
//                selectHuman = humanInfo;
//                break;
//            }
//        }
//        if (selectHuman == null) {
//            //走到这应该是客户端发来错误的角色ID
//            return;
//        }
//
//        player.setStatus(LoginStatus.LoadingHuman);
//        sGlobals.getRedisProcessUnit().submitTask(
//                new LoadHumanDataTask(selectHuman, player, sGlobals, playerManagerArgs));
//    }
//
//}
