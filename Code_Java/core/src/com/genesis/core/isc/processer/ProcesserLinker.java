package com.genesis.core.isc.processer;

import com.genesis.core.isc.remote.IRemote;

import java.util.ArrayList;


public final class ProcesserLinker implements IProcessLinker {

    private ArrayList<IActorProcesser> processerList = new ArrayList<>();

    public ProcesserLinker addLast(IActorProcesser processer) {
        this.processerList.add(processer);
        return this;
    }

    @Override
    public void execute(IRemote sender, Object msg) {
        for (IActorProcesser each : processerList) {
            if (each.onReceived(sender, msg) == ProcessResult.FINISH) {
                return;
            }
        }
    }
}
