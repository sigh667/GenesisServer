package com.mokylin.bleach.test.common.concurrent;

import java.util.concurrent.Semaphore;

public class Collaborator {
	
	private Semaphore sp = new Semaphore(1);
	
	public void start(){
		try {
			sp.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void done(){
		if(sp.availablePermits() == 1) return;
		sp.release();
	}
	
	public void waitForDone(){
		try {
			sp.acquire();
			sp.release();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
