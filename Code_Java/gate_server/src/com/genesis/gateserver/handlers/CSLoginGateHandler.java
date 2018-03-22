package com.genesis.gateserver.handlers;

import com.genesis.gateserver.core.OnlinePlayerContainer;
import com.genesis.gateserver.global.Globals;
import com.genesis.network2client.auth.AuthUtil;
import com.genesis.network2client.process.IClientMsgHandler;
import com.genesis.network2client.session.IClientSession;
import com.genesis.protobuf.LoginMessage;
import com.genesis.redis.center.RedisLoginKey;
import com.genesis.redis.center.data.LoginClientInfo;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Objects;

/**
 * Client登陆Gate
 * <p>2018-03-07 20:39
 *
 * @author Joey
 **/
public class CSLoginGateHandler implements IClientMsgHandler<LoginMessage.CSLoginGate> {

    @Override
    public void handle(IClientSession session, LoginMessage.CSLoginGate csLoginGate) {
        final String accountId = csLoginGate.getAccountId();
        final String channel = csLoginGate.getChannel();
        final List<Integer> vCodeList = csLoginGate.getVerificationCodeList();
        final RedissonClient redisson = Globals.getRedisson();

        // 加锁
        if (!AuthUtil.lock(channel, accountId, redisson)) {
            notifyFailAndDisconnect(session, LoginMessage.LoginGateFailReason.YOUR_ACCOUNT_LOGINING);
            return;
        }

        // 组装待登陆的玩家key
        final String keyToGate = RedisLoginKey.ToGate.builderKey(channel, accountId);
        final RBucket<LoginClientInfo> bucketToGate = redisson.getBucket(keyToGate);
        final LoginClientInfo loginClientInfo = bucketToGate.get();
        for (int i = 0; i < loginClientInfo.vCode.size(); i++) {
            if (!Objects.equals(loginClientInfo.vCode.get(i), vCodeList.get(i))) {
                notifyFailAndDisconnect(session, LoginMessage.LoginGateFailReason.VCODE_WRONG);
                return;
            }
        }

        // 验证通过
        // 在Redis中，标记该账号在本Gate中
        final String key = RedisLoginKey.InGate.builderKey(channel, accountId);
        final RBucket<Integer> bucketInGate = redisson.getBucket(key);
        bucketInGate.set(Globals.getGateInfo().id);
        // 删除验证信息
        bucketToGate.delete();

        // 解锁
        if (!AuthUtil.unlock(channel, accountId, redisson)) {
            notifyFailAndDisconnect(session, LoginMessage.LoginGateFailReason.YOUR_ACCOUNT_LOGINING);
            return;
        }

        // 标记登陆状态
        OnlinePlayerContainer.onLogin(channel, accountId, session);
    }

    /**
     * 通知客户端，登陆Gate失败，并告知原因
     * @param session   客户端连接
     * @param reason    失败原因
     */
    private void notifyFailAndDisconnect(IClientSession session, LoginMessage.LoginGateFailReason reason) {
        LoginMessage.SCLoginGateFail.Builder builder = LoginMessage.SCLoginGateFail.newBuilder();
        builder.setFailReason(reason);
        session.sendMessage(builder);

        // 断开连接
        session.disconnect();
    }
}
