package com.mokylin.bleach.test.dataserver.gamedb;

import java.util.Date;

import com.mokylin.bleach.test.common.db.Table;
import com.mokylin.bleach.test.common.db.TableContent;


public class HumanTable extends Table{
	
	public HumanTable(){
		this.tableName = "t_human";
		this.content = new TableContent("id", "channel", "accountId", "originalServerId", "currentServerId", "name", "createTime", "level", "chargeDiamond", "freeDiamond", "gold");
		this.content.values(
				1,	"",	"1",	2001,	2001,	"test",	new Date(837039928046L),	15,	100,	100,	20,
				2,	"",	"2",	2001,	2001,	"ts",	new Date(837039928046L),	80,	100,	100,	20,
				3,	"",	"2",	2002,	2001,	"ts2",	new Date(837039928046L),	71,	100,	100,	20,
				4,	"",	"3",	2002,	2001,	"what",	new Date(837039928046L),	56,	100,	100,	20
		);
	}
}
