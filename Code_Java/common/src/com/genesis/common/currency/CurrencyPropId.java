package com.genesis.common.currency;

import com.genesis.common.prop.IProp;
import com.genesis.common.prop.PropType;
import com.genesis.common.prop.IPropNotifier;
import com.genesis.common.prop.PropScope;

import java.util.Map;

/**
 * 金币枚举
 *
 * @author yaguang.xiao
 *
 */
public enum CurrencyPropId implements IProp {
    /**充值的钻石*/
    CHARGE_DIAMOND(0, Long.MAX_VALUE), /**免费钻石*/
    FREE_DIAMOND(0, Long.MAX_VALUE), /** 累计充值钻石 */
    ACCUMULATED_CHARGE_DIAMOND(0, Long.MAX_VALUE), /** 累计消费的充值钻石 */
    ACCUMULATED_CONSUMED_CHARGE_DIAMOND(0, Long.MAX_VALUE), /**金币*/
    GOLD(0, Long.MAX_VALUE),;

    private static Map<String, CurrencyPropId> reflect =
            PropType.constructReflect(CurrencyPropId.class, PropType.CURRENCY);
    public final PropScope scope;

    private CurrencyPropId(long minValue, long maxValue) {
        this.scope = new PropScope(minValue, maxValue);
    }

    /**
     * 获取数字对应的金钱枚举
     *
     * @param idName
     * @return
     */
    public static CurrencyPropId get(String idName) {
        return reflect.get(idName);
    }

    /**
     * 判断指定的索引是否有被定义
     * @param idIndex
     * @return
     */
    public static boolean containsIndex(int idIndex) {
        return reflect.containsKey(idIndex);
    }

    @Override
    public PropType getPropType() {
        return PropType.CURRENCY;
    }

    /**
     * 通知玩家获取金钱成功
     * @param human
     * @param getValue
     */
    public void notifyMoneyGet(IPropNotifier human, long getValue) {
        //TODO 在这里给玩家发送得到金钱的通知消息
    }

}
