package com.mokylin.bleach.test.log.performance;

public class TestClass {
	private long iEventId;
	private long iWorldId;
	private long iUin;
	private long deEventTime;
	private long dtLoginTime;
	private String vClientIp;
	private String vZoneId;
	private long dtCreateTime;
	private long iOnlineTime;
	private long iOnlineTotalTime;
	private long iRoleId;
	private String vRoleName;
	private int iRoleLevel;
	private long iRoleExp;
	private long iRepute;
	private long iMainSpeExp;
	private long iMoney;
	private long iAccumDeposit;
	private long iAssumConsume;
	private long iGameTime;
	private int iLoginWay;
	
	public TestClass(long iEventId, long iWorldId, long iUin,
			long deEventTime, long dtLoginTime, String vClientIp,
			String vZoneId, long dtCreateTime, long iOnlineTime,
			long iOnlineTotalTime, long iRoleId, String vRoleName,
			int iRoleLevel, long iRoleExp, long iRepute, long iMainSpeExp,
			long iMoney, long iAccumDeposit, long iAssumConsume,
			long iGameTime, int iLoginWay) {
		this.iEventId = iEventId;
		this.iWorldId = iWorldId;
		this.iUin = iUin;
		this.deEventTime = deEventTime;
		this.dtLoginTime = dtLoginTime;
		this.vClientIp = vClientIp;
		this.vZoneId = vZoneId;
		this.dtCreateTime = dtCreateTime;
		this.iOnlineTime = iOnlineTime;
		this.iOnlineTotalTime = iOnlineTotalTime;
		this.iRoleId = iRoleId;
		this.vRoleName = vRoleName;
		this.iRoleLevel = iRoleLevel;
		this.iRoleExp = iRoleExp;
		this.iRepute = iRepute;
		this.iMainSpeExp = iMainSpeExp;
		this.iMoney = iMoney;
		this.iAccumDeposit = iAccumDeposit;
		this.iAssumConsume = iAssumConsume;
		this.iGameTime = iGameTime;
		this.iLoginWay = iLoginWay;
	}
}
