package com.mokylin.bleach.test.dataserver.gamedb;

import com.mokylin.bleach.test.common.db.Table;
import com.mokylin.bleach.test.common.db.TableContent;

public class ArenaSnapTable extends Table {

	public ArenaSnapTable() {
		this.tableName = "t_arena_snap";
		this.content = new TableContent("id", "name", "level", "arenaRank");
		this.content.values(
				1,	"test",		15,	3,
				2,	"ts",		80,	1,
				3,	"ts2",		71,	4,
				4,	"what",		56,	2
				);
	}
}
