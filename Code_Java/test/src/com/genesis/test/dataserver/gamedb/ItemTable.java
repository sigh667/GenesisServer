package com.genesis.test.dataserver.gamedb;

import com.genesis.test.common.db.Table;
import com.genesis.test.common.db.TableContent;

import java.util.Date;

public class ItemTable extends Table {

    public ItemTable() {
        this.tableName = "t_item";
        this.content = new TableContent("id", "humanId", "templateId", "overlap", "createTime");
        this.content.values(1, 1, 9001, 21, new Date(837039928046L), 2, 1, 9002, 22,
                new Date(837039928046L), 3, 2, 9003, 23, new Date(837039928046L), 4, 3, 9004, 24,
                new Date(837039928046L));
    }
}
