package com.mokylin.bleach.test.core.event;

import com.mokylin.bleach.core.event.IEventListener;

public class TestEventListener implements IEventListener<TestEvent> {

	@Override
	public void onEventOccur(TestEvent event) {
		TestEventContext.totalNum += event.num;
	}

}
