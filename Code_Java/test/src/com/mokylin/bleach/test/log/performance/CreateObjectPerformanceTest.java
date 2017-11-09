package com.mokylin.bleach.test.log.performance;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 对象创建性能测试
 * 
 * @author yaguang.xiao
 * 
 */
public class CreateObjectPerformanceTest {

	public static void main(String[] args) {

		List<TestClass> objList = Lists.newLinkedList();
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			sendLog(objList, 123514512341234l, 123543112341234l, 1234146512341l,
					12345124351425l, 12435143512435l, "127.0.0.1", "1234",
					23452345l, 234532452345l, 23452345l, 23452345l, "roleName",
					12, 1453, 25425234l, 1452345l, 234523452345l, 23452345234l,
					23452345l, 23452345l, 2345);
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("total time : " + time + " milliseconds.");

	}

	private static void sendLog(List<TestClass> objList, long iEventId, long iWorldId, long iUin,
			long deEventTime, long dtLoginTime, String vClientIp,
			String vZoneId, long dtCreateTime, long iOnlineTime,
			long iOnlineTotalTime, long iRoleId, String vRoleName,
			int iRoleLevel, long iRoleExp, long iRepute, long iMainSpeExp,
			long iMoney, long iAccumDeposit, long iAssumConsume,
			long iGameTime, int iLoginWay) {
		objList.add(new TestClass(iEventId, iWorldId, iUin, deEventTime, dtLoginTime,
				vClientIp, vZoneId, dtCreateTime, iOnlineTime,
				iOnlineTotalTime, iRoleId, vRoleName, iRoleLevel, iRoleExp,
				iRepute, iMainSpeExp, iMoney, iAccumDeposit, iAssumConsume,
				iGameTime, iLoginWay));
	}

}
