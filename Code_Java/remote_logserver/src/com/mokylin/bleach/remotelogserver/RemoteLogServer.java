package com.mokylin.bleach.remotelogserver;

import org.apache.thrift.transport.TTransportException;

public class RemoteLogServer {

	public static void main(String[] args1) {
		Globals.getMainthread().execute(new Runnable() {

			@Override
			public void run() {
				try {
					Globals.init();
				} catch (TTransportException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}

}
