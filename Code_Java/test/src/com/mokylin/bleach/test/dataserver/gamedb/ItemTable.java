package com.mokylin.bleach.test.dataserver.gamedb;

import java.util.Date;

import com.mokylin.bleach.test.common.db.Table;
import com.mokylin.bleach.test.common.db.TableContent;

public class ItemTable extends Table {

	public ItemTable() {
		this.tableName = "t_item";
		this.content = new TableContent("id", "humanId", "templateId", "overlap", "createTime");
		this.content.values(
				1,	1,	9001,	21,	new Date(837039928046L),
				2,	1,	9002,	22,	new Date(837039928046L),
				3,	2,	9003,	23,	new Date(837039928046L),
				4,	3,	9004,	24,	new Date(837039928046L)	
		);
	}
}
