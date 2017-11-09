package com.mokylin.bleach.test.log.performance;

/**
 * 字符串拼接性能测试
 * 
 * @author yaguang.xiao
 * 
 */
public class StringConcatPerformanceTest {

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			sendLog(123514512341234l, 123543112341234l, 1234146512341l,
					12345124351425l, 12435143512435l, "127.0.0.1", "1234",
					23452345l, 234532452345l, 23452345l, 23452345l, "roleName",
					12, 1453, 25425234l, 1452345l, 234523452345l, 23452345234l,
					23452345l, 23452345l, 2345);
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("total time : " + time + " milliseconds.");

	}

	private static void sendLog(long iEventId, long iWorldId, long iUin,
			long deEventTime, long dtLoginTime, String vClientIp,
			String vZoneId, long dtCreateTime, long iOnlineTime,
			long iOnlineTotalTime, long iRoleId, String vRoleName,
			int iRoleLevel, long iRoleExp, long iRepute, long iMainSpeExp,
			long iMoney, long iAccumDeposit, long iAssumConsume,
			long iGameTime, int iLoginWay) {
		StringBuilder sb = new StringBuilder();
		sb.append(iEventId).append("|").append(iWorldId).append("|")
				.append(iUin).append("|").append(deEventTime).append("|")
				.append(dtLoginTime).append("|").append(vClientIp).append("|")
				.append(vZoneId).append("|").append(dtCreateTime).append("|")
				.append(iOnlineTime).append("|").append(iOnlineTotalTime)
				.append("|").append(iRoleId).append("|").append(vRoleName)
				.append("|").append(iRoleLevel).append("|").append(iRoleExp)
				.append("|").append(iRepute).append("|").append(iMainSpeExp)
				.append("|").append(iMoney).append("|").append(iAccumDeposit)
				.append("|").append(iAssumConsume).append("|")
				.append(iGameTime).append("|").append(iLoginWay);
		String logStr = sb.toString();
	}

}
