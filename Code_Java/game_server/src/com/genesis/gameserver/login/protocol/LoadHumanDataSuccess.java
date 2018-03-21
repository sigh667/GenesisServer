package com.genesis.gameserver.login.protocol;

import com.genesis.gamedb.human.HumanData;
import com.genesis.gameserver.player.Player;

/**
 * 加载玩家角色成功。<p>
 *
 * 发往PlayerManagerActor。
 *
 * @author pangchong
 *
 */
public class LoadHumanDataSuccess {

    public final HumanData humanData;

    public final Player player;

    public LoadHumanDataSuccess(HumanData humanData, Player player) {
        this.humanData = humanData;
        this.player = player;
    }

}
