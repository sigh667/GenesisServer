package com.mokylin.bleach.test.core.serializer.mockentity;

public class Message1 {
	
	private int id;
	private int id2;
	private int id3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public int getId3() {
		return id3;
	}

	public void setId3(int id3) {
		this.id3 = id3;
	}
	
	@Override
	public String toString() {
		return "[id=" + this.id + ", id2=" + this.id2 + ", id3=" + this.id3 + "]";
	}
}
