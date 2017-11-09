package com.mokylin.bleach.dblog;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.mokylin.bleach.dblog.anno.ColumnView;
import com.mokylin.bleach.dblog.anno.Index;

/**
 * 玩家日志的基类
 * 
 * @author yaguang.xiao
 *
 */

@MappedSuperclass
public abstract class HumanDbLog extends DbLog {

	private static final long serialVersionUID = 1L;

	@ColumnView(value = "账号ID", search = "=")
	@Index
	@Column
	private long accountId; // 账号信息

	@ColumnView(value = "账号名", search = "=")
	@Index
	@Column(length = 128)
	private String accountName;

	@ColumnView(value = "角色ID", search = "=")
	@Index
	@Column
	private long charId; // 角色信息

	@ColumnView(value = "角色名", search = "=")
	@Index
	@Column(length = 128)
	private String charName;

	@ColumnView("级别")
	@Column
	private int level; // 用户当前级别

	@ColumnView(value = "职业", optionName = { "无职业", "大剑 ", "游侠", "弓箭", "战士", "法师" }, optionValue = { 0, 1, 2, 4, 8, 16 })
	@Column
	private int allianceId; // 玩家职业

	@ColumnView("VIP等级")
	@Column
	private int vipLevel; // 玩家的VIP等级

	public HumanDbLog() {}

	public HumanDbLog(long time, int rid, int sid, long aid, String accountName, long cid, String charName, int level,
			int allianceId, int vipLevel, int reason, String param) {
		super(time, rid, sid, reason, param);
		this.accountId = aid;
		this.accountName = accountName;
		this.charId = cid;
		this.charName = charName;
		this.level = level;
		this.allianceId = allianceId;
		this.vipLevel = vipLevel;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getCharId() {
		return charId;
	}

	public void setCharId(long charId) {
		this.charId = charId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAllianceId() {
		return allianceId;
	}

	public void setAllianceId(int allianceId) {
		this.allianceId = allianceId;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCharName() {
		return charName;
	}

	public void setCharName(String charName) {
		this.charName = charName;
	}

}
