package com.genesis.core.isc.processer;

import com.genesis.core.isc.remote.IRemote;

public interface IProcessLinker {

    public void execute(IRemote sender, Object msg);
}
