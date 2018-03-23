package com.genesis.servermsg.core.isc.processer;

import com.genesis.servermsg.core.isc.remote.IRemote;

public interface IProcessLinker {

    public void execute(IRemote sender, Object msg);
}
