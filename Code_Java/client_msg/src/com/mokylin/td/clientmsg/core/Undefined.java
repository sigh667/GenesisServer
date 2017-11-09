package com.mokylin.td.clientmsg.core;

public class Undefined implements Comparable<Undefined> {

	@Override
	public int compareTo(Undefined o) {
		if(o instanceof Undefined)
		{
			return 0;
		}
		return -1;
	}

}
