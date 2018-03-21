package com.genesis.test.gameserver.timeaxis.task;

import com.genesis.core.timeaxis.ITimeEvent;
import com.genesis.core.timeaxis.ITimeEventType;
import com.genesis.test.gameserver.timeaxis.Human;
import com.genesis.test.gameserver.timeaxis.TestTimeEventType;

/**
 * 时间轴任务实现模板
 * @author yaguang.xiao
 *
 */
public class SimpleTimeAxisEventImpl implements ITimeEvent<Human> {
    public static volatile int eventOccured = 0;

    @Override
    public ITimeEventType getEventType() {
        return TestTimeEventType.TEST;
    }

    @Override
    public long getEventId() {
        return 0;
    }

    @Override
    public void eventOccur(Human timeAxisHost) {
        eventOccured++;
        System.out.println("event occured!!!");
    }
}
