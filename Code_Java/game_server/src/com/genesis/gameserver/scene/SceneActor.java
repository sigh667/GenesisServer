package com.genesis.gameserver.scene;

import com.genesis.servermsg.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.protobuf.MessageType.MessageTarget;

import akka.actor.UntypedActor;

import static com.google.common.base.Preconditions.checkNotNull;

@MessageAcception(MessageTarget.SCENE)
public class SceneActor extends UntypedActor {

    public static final String ACTOR_NAME = "scene";

    private final ServerGlobals sGlobals;

    public SceneActor(ServerGlobals sGlobals) {
        this.sGlobals = checkNotNull(sGlobals, "SceneActor can not init with null Server Globals!");
    }

    @Override
    public void onReceive(Object msg) throws Exception {

    }

}
