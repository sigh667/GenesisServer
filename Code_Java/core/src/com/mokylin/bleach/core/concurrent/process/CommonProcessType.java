package com.mokylin.bleach.core.concurrent.process;

import static com.mokylin.bleach.core.concurrent.process.ProcessUnitHelper.createScheduledProcessUnit;
import static com.mokylin.bleach.core.concurrent.process.ProcessUnitHelper.createSingleProcessUnit;

public interface CommonProcessType {
	
	public ProcessUnit MAIN = createSingleProcessUnit("Game Server Main Process Unit");
	
	public ScheduledProcessUnit SCHEDULED = createScheduledProcessUnit("Scheduled Process Unit", Runtime.getRuntime().availableProcessors());
}
