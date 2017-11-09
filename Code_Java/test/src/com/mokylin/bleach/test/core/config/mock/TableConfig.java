package com.mokylin.bleach.test.core.config.mock;

import com.google.common.collect.Table;

public class TableConfig {

	private Table<TestEnum, Integer, TestClass> table;

	public Table<TestEnum, Integer, TestClass> getTable() {
		return table;
	}

	public void setTable(Table<TestEnum, Integer, TestClass> table) {
		this.table = table;
	}

}
