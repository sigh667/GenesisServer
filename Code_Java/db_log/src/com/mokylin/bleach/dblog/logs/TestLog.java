package com.mokylin.bleach.dblog.logs;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.mokylin.bleach.dblog.HumanDbLog;
import com.mokylin.bleach.dblog.anno.ColumnView;
import com.mokylin.bleach.dblog.anno.TableView;

@Entity
@TableView("测试日志")
public class TestLog extends HumanDbLog {

	private static final long serialVersionUID = 1L;

	@ColumnView(value = "测试字段")
	@Column
	private String testField;

	public String getTestField() {
		return testField;
	}

	public void setTestField(String testField) {
		this.testField = testField;
	}

}
