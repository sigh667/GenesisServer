package com.genesis.gateserver.core.frontend.gameserver;

import com.google.common.base.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class GameServerFrontManager {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ConcurrentHashMap<Integer, GameServerFrontend> gameServerMap;

    public GameServerFrontManager() {
        this.gameServerMap = new ConcurrentHashMap<>();
    }

    public boolean publish(GameServerFrontend gameServer) {
        GameServerFrontend pre =
                gameServerMap.putIfAbsent(gameServer.getGameServerId(), gameServer);
        if (pre == null) {
            return true;
        }

        log.warn("GameServer [{}] publishes more than once!", pre.getGameServerId());
        return false;
    }

    public Optional<GameServerFrontend> getGameServerFrontend(int sId) {
        return Optional.fromNullable(gameServerMap.get(sId));
    }

}
