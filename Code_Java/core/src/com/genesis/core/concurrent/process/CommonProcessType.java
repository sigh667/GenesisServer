package com.genesis.core.concurrent.process;

import static com.genesis.core.concurrent.process.ProcessUnitHelper.createScheduledProcessUnit;
import static com.genesis.core.concurrent.process.ProcessUnitHelper.createSingleProcessUnit;

public interface CommonProcessType {

    public ProcessUnit MAIN = createSingleProcessUnit("Game Server Main Process Unit");

    public ScheduledProcessUnit SCHEDULED = createScheduledProcessUnit("Scheduled Process Unit",
            Runtime.getRuntime().availableProcessors());
}
