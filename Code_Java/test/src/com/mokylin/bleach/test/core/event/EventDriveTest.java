package com.mokylin.bleach.test.core.event;

import com.google.common.collect.Lists;

import com.genesis.core.event.EventBus;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EventDriveTest {

    @Test
    public void the_occur_count_should_be_right()
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        EventBus e = new EventBus(Lists.newArrayList("com.mokylin.bleach"));

        e.occurs(new TestEvent(1));
        e.occurs(new TestEvent(2));
        assertThat(TestEventContext.totalNum, is(3));
    }
}
