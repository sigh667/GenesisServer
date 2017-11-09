package com.mokylin.bleach.test.core.config.serverconfig;

public class LogInfo {
	private int info1 = 23;
	private String info2 = "info2";

	public int getInfo1() {
		return info1;
	}

	public String getInfo2() {
		return info2;
	}
	
	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append(" {\n");
		content.append("\tinfo1 = " + info1 + "\n");
		content.append("\tinfo2 = " + info2 + "\n");
		content.append("}\n");
		return content.toString();
	}

}
