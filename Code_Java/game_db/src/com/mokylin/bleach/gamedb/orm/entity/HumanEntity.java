package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.HumanKey;


@Entity
@Table(name = "t_human")
public class HumanEntity implements EntityWithRedisKey<HumanKey>, IHumanRelatedEntity {

	/***/
	private static final long serialVersionUID = 1L;

	/**角色ID*/
	private Long id;
	/**渠道*/
	private String channel;
	/**所属的账号ID*/
	private String accountId;
	/**初始服务器ID*/
	private int originalServerId;
	/**当前服务器ID*/
	private int currentServerId;
	/**名字*/
	private String name;
	/**创建时间*/
	private Timestamp createTime;
	/** 本次登陆时间 */
	private Timestamp loginTime;
	/** 总在线时长 */
	private long totalOnlineTime;
	/**等级*/
	private long level;
	/**当前经验值*/
	private long exp;
	/**当前体力*/
	private long energy;
	/**最后一次体力恢复时间*/
	private Timestamp lastEnergyRecoverTime;
	/**当日购买体力的次数*/
	private long buyEnergyCounts;
	/**最后一次体力购买次数重置时间*/
	private Timestamp lastBuyEnergyCountsResetTime;
	/**VIP等级*/
	private long vipLevel;
	/**VIP经验值*/
	private long vipExp;

	/** 累计充值钻石 */
	private long accumulatedChargeDiamond;
	/** 累计消费的充值钻石 */
	private long accumulatedConsumedChargeDiamond;
	/** 充值钻石 */
	private long chargeDiamond;
	/** 免费钻石 */
	private long freeDiamond;
	/** 金币 */
	private long gold;
	/** 当月领取的每日奖励的次数（签到次数） */
	private byte timesOfDailyReward;
	/** 最后一次领取每日奖励的时间 */
	private Timestamp lastDailyRewardTime;

	@Override
	public HumanKey newRedisKey(Integer serverId) {
		return new HumanKey(serverId,this.getId());
	}
	@Override
	public long humanId() {
		return this.id;
	}
	public HumanInfo buildHumanInfo() {
		HumanInfo humanInfo = new HumanInfo();
		humanInfo.setId(this.getId());
		humanInfo.setName(this.getName());
		humanInfo.setOriginalServerId(this.getOriginalServerId());
		humanInfo.setChannel(this.getChannel());
		humanInfo.setAccountId(this.getAccountId());
		return humanInfo;
	}

	@Id
	@Column
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	@Column
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@Column
	public int getOriginalServerId() {
		return originalServerId;
	}
	public void setOriginalServerId(int originalServerId) {
		this.originalServerId = originalServerId;
	}

	@Column
	public int getCurrentServerId() {
		return currentServerId;
	}
	public void setCurrentServerId(int currentServerId) {
		this.currentServerId = currentServerId;
	}

	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(columnDefinition="smallint(5) unsigned")
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	
	@Column
	public long getTotalOnlineTime() {
		return totalOnlineTime;
	}
	public void setTotalOnlineTime(long totalOnlineTime) {
		this.totalOnlineTime = totalOnlineTime;
	}
	
	@Column(columnDefinition="smallint(5) unsigned")
	public long getLevel() {
		return level;
	}
	public void setLevel(long level) {
		this.level = level;
	}

	@Column
	public long getAccumulatedChargeDiamond() {
		return accumulatedChargeDiamond;
	}
	public void setAccumulatedChargeDiamond(long accumulatedChargeDiamond) {
		this.accumulatedChargeDiamond = accumulatedChargeDiamond;
	}
	
	@Column
	public long getAccumulatedConsumedChargeDiamond() {
		return accumulatedConsumedChargeDiamond;
	}
	public void setAccumulatedConsumedChargeDiamond(long accumulatedConsumedChargeDiamond) {
		this.accumulatedConsumedChargeDiamond = accumulatedConsumedChargeDiamond;
	}
	
	@Column
	public long getChargeDiamond() {
		return chargeDiamond;
	}
	public void setChargeDiamond(long chargeDiamond) {
		this.chargeDiamond = chargeDiamond;
	}
	@Column
	public long getFreeDiamond() {
		return freeDiamond;
	}
	public void setFreeDiamond(long freeDiamond) {
		this.freeDiamond = freeDiamond;
	}
	@Column
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	@Column
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	@Column(columnDefinition="int(11)")
	public long getEnergy() {
		return energy;
	}
	public void setEnergy(long energy) {
		this.energy = energy;
	}
	@Column(columnDefinition="tinyint(4)")
	public byte getTimesOfDailyReward() {
		return (byte) timesOfDailyReward;
	}
	public void setTimesOfDailyReward(int timesOfDailyReward) {
		this.timesOfDailyReward = (byte) timesOfDailyReward;
	}
	@Column
	public Timestamp getLastDailyRewardTime() {
		return lastDailyRewardTime;
	}
	public void setLastDailyRewardTime(Timestamp lastDailyRewardTime) {
		this.lastDailyRewardTime = lastDailyRewardTime;
	}
	@Column
	public Timestamp getLastEnergyRecoverTime() {
		return lastEnergyRecoverTime;
	}
	public void setLastEnergyRecoverTime(Timestamp lastEnergyRecoverTime) {
		this.lastEnergyRecoverTime = lastEnergyRecoverTime;
	}
	@Column(columnDefinition="smallint(5) unsigned")
	public long getBuyEnergyCounts() {
		return buyEnergyCounts;
	}
	public void setBuyEnergyCounts(long buyEnergyCounts) {
		this.buyEnergyCounts = buyEnergyCounts;
	}
	@Column
	public Timestamp getLastBuyEnergyCountsResetTime() {
		return lastBuyEnergyCountsResetTime;
	}
	public void setLastBuyEnergyCountsResetTime(
			Timestamp lastBuyEnergyCountsResetTime) {
		this.lastBuyEnergyCountsResetTime = lastBuyEnergyCountsResetTime;
	}
	
	
	@Column(columnDefinition="tinyint(3) unsigned")
	public long getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(long vipLevel) {
		this.vipLevel = vipLevel;
	}
	@Column
	public long getVipExp() {
		return vipExp;
	}
	public void setVipExp(long vipExp) {
		this.vipExp = vipExp;
	}

}
