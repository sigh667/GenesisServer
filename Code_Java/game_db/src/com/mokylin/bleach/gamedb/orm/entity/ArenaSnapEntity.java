package com.mokylin.bleach.gamedb.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ArenaSnapKey;

/**
 * 竞技场镜像
 */
@Entity
@Table(name = "t_arena_snap")
public class ArenaSnapEntity implements EntityWithRedisKey<ArenaSnapKey>, IHumanRelatedEntity {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**角色ID 主键*/
	private long id;
	/**角色名称，战斗显示用*/
	private String name;
	/**角色等级，战斗显示用*/
	private int level;
	/**竞技场排名*/
	private int arenaRank;

	@Override
	public ArenaSnapKey newRedisKey(Integer serverId) {
		return new ArenaSnapKey(serverId,this.getId());
	}

	@Id
	@Column
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column
	public int getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(int arenaRank) {
		this.arenaRank = arenaRank;
	}

	@Override
	public long humanId() {
		return this.id;
	}

}
