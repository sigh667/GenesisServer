package com.mokylin.bleach.core.isc.processer;

import java.util.ArrayList;

import com.mokylin.bleach.core.isc.remote.IRemote;


public final class ProcesserLinker implements IProcessLinker{
	
	private ArrayList<IActorProcesser> processerList = new ArrayList<>();
	
	public ProcesserLinker addLast(IActorProcesser processer){
		this.processerList.add(processer);
		return this;
	}

	@Override
	public void execute(IRemote sender, Object msg) {
		for(IActorProcesser each : processerList){
			if(each.onReceived(sender, msg) == ProcessResult.FINISH) return;
		}		
	}
}
