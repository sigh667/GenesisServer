package com.mokylin.bleach.test.core.redis;

public class MockEntity {

	private String name;
	private long id;
	private int exp;
	private String japaneseName;
	
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final long getId() {
		return id;
	}
	public final void setId(long id) {
		this.id = id;
	}
	public final int getExp() {
		return exp;
	}
	public final void setExp(int exp) {
		this.exp = exp;
	}
	public final String getJapaneseName() {
		return japaneseName;
	}
	public final void setJapaneseName(String japaneseName) {
		this.japaneseName = japaneseName;
	}
	
}
