package com.mokylin.bleach.test.core.serializer.mockentity;

import java.util.List;

public class ListMessage {

	public final List<Message> entities;
	
	public ListMessage(List<Message> entities) {
		this.entities = entities;
	}
}
