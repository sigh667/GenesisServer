package com.mokylin.bleach.gamedb.human;

/**
 * 角色的简要信息
 * @author baoliang.shen
 *
 */
public class HumanInfo {
	/**角色ID*/
	private Long id;
	/**渠道*/
	private String channel;
	/**账号ID*/
	private String accountId;
	/**初始服务器ID*/
	private int originalServerId;
	/**名字*/
	private String name;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getOriginalServerId() {
		return originalServerId;
	}
	public void setOriginalServerId(int originalServerId) {
		this.originalServerId = originalServerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
}
