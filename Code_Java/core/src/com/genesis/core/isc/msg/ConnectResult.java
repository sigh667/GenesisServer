package com.genesis.core.isc.msg;

import com.genesis.core.isc.remote.actorrefs.IActorPackages;

import java.io.Serializable;

/**
 * 响应{@link Connect}的应答消息。
 *
 * @author pangchong
 *
 */
public final class ConnectResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public final IActorPackages actorPackages;

    public ConnectResult(IActorPackages actorPackages) {
        this.actorPackages = actorPackages;
    }
}
