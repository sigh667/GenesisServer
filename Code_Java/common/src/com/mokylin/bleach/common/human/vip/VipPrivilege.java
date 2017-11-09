package com.mokylin.bleach.common.human.vip;

/**
 * VIP特权
 * @author ChangXiao
 *
 */
public class VipPrivilege {
	public static final int DISABLED = -1;
	private int status;
	
	public VipPrivilege(int status) {
		this.status = status;
	}
	
	/**
	 * 判断该特权是否开启
	 * @return
	 */
	public boolean isOpen(){
		return status != DISABLED;
	}
	
	/**
	 * 获取具体特权值，如体力购买次数，应先调用isOpen()方法判断特权是否可用
	 * @return 正整数|-1；返回-1则该特权不可用
	 */
	public int getValue(){
		return status;
	}
}
