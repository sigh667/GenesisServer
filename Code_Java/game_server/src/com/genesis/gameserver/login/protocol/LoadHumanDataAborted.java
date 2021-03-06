package com.genesis.gameserver.login.protocol;

import com.genesis.gameserver.player.Player;

/**
 * 放弃加载玩家角色。<p>
 *
 * 发往PlayerManagerActor。
 *
 * @author pangchong
 *
 */
public class LoadHumanDataAborted {

    public final Player player;

    public LoadHumanDataAborted(Player player) {
        this.player = player;
    }

}
