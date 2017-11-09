package com.mokylin.bleach.gameserver.arena;

import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gameserver.core.persistance.IDataUpdater;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;

/**
 * 竞技场镜像
 * @author baoliang.shen
 *
 */
public class ArenaSnap extends ObjectInSqlImpl<Long, ArenaSnapEntity> {

	public ArenaSnap(IDataUpdater dataUpdater) {
		super(dataUpdater);
	}

	/**角色ID 主键*/
	private long id;
	/**角色名称，战斗显示用*/
	private String name;
	/**角色等级，战斗显示用*/
	private int level;
	/**竞技场排名*/
	private int arenaRank;

	@Override
	public Long getDbId() {
		return this.id;
	}

	@Override
	public ArenaSnapEntity toEntity() {
		ArenaSnapEntity arenaSnapEntity = new ArenaSnapEntity();
		arenaSnapEntity.setArenaRank(arenaRank);
		arenaSnapEntity.setId(id);
		arenaSnapEntity.setLevel(level);
		arenaSnapEntity.setName(name);
		return arenaSnapEntity;
	}

	@Override
	public void fromEntity(ArenaSnapEntity entity) {
		this.arenaRank = entity.getArenaRank();
		this.id = entity.getId();
		this.level = entity.getLevel();
		this.name = entity.getName();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(int arenaRank) {
		this.arenaRank = arenaRank;
	}

}
