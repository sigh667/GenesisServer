package com.mokylin.bleach.launcher;

import com.mokylin.bleach.agentserver.AgentServer;
import com.mokylin.bleach.dataserver.DataServer;
import com.mokylin.bleach.gameserver.GameServer;

public class Launcher {
	
	/** 数据服务器启动参数前缀 */
	private static final String DS_PREFIX = "-ds";
	/** 网关服务器启动参数前缀 */
	private static final String AS_PREFIX = "-as";
	/** 游戏服务器启动参数前缀 */
	private static final String GS_PREFIX = "-gs";

	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[]{DS_PREFIX, AS_PREFIX, GS_PREFIX};
		}

		for (String arg : args) {
			if (arg.startsWith(DS_PREFIX)) {
				arg = arg.replaceFirst(DS_PREFIX, "");
				startServer(DataServer.class, arg);
			}
			if (arg.startsWith(AS_PREFIX)) {
				arg = arg.replaceFirst(AS_PREFIX, "");
				startServer(AgentServer.class, arg);
			}
			if (arg.startsWith(GS_PREFIX)) {
				arg = arg.replaceFirst(GS_PREFIX, "");
				startServer(GameServer.class, arg);
			}
		}
	}
	
	private static void startServer(Class<?> clazz, String arg) {
		String[] args = new String[0];
		// 如果参数已":"开头,表示该服务器有启动参数,用","分隔
		if (arg.startsWith(":")) {
			args = arg.replaceFirst(":", "").split(",");
		}
		try {
			clazz.getMethod("main", String[].class).invoke(clazz, new Object[]{args});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
