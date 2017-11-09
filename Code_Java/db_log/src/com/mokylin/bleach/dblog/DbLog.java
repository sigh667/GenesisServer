package com.mokylin.bleach.dblog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.mokylin.bleach.dblog.anno.ColumnView;

/**
 * 所有日志的基类
 * 
 * @author yaguang.xiao
 *
 */

@MappedSuperclass
public abstract class DbLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志表名
	 */
	@Transient
	private String tableName;

	@ColumnView("日志ID")
	@Id
	private int id;

	@ColumnView(value = "时间", search = ">=,<=", orderBy = true, time = true)
	private long logTime;

	@ColumnView("服务器区ID")
	@Column
	private int regionId;

	@ColumnView("服务器ID")
	@Column
	private int serverId;

	@ColumnView(value = "原因", search = "=")
	@Column
	private int reason;

	public DbLog() {}

	public DbLog(long time, int rid, int sid, int reason, String param) {
		this.logTime = time;
		this.regionId = rid;
		this.serverId = sid;
		this.reason = reason;
	}

	public static String formatName(Class<?> clazz) {
		return formatName(clazz.getSimpleName());
	}

	public static String formatName(String name) {
		name = name.substring(0, 1).toLowerCase() + name.substring(1);

		return name.replaceAll("([A-Z]{1})", "_$1").toLowerCase();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLogTime() {
		return logTime;
	}

	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
