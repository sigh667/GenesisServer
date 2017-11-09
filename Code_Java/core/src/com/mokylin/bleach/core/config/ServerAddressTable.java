package com.mokylin.bleach.core.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.model.ServerInfo;
import com.mokylin.bleach.core.isc.ServerType;

public class ServerAddressTable {

	private Table<ServerType, Integer, ServerInfo> table;
	
	public ServerAddressTable(){
		this(HashBasedTable.<ServerType, Integer, ServerInfo>create());
	}
	
	public ServerAddressTable(Table<ServerType, Integer, ServerInfo> table) {
		this.table = checkNotNull(table);
	}

	public Table<ServerType, Integer, ServerInfo> getTable() {
		return table;
	}

	public void setTable(Table<ServerType, Integer, ServerInfo> table) {
		this.table = table;
	}

	public static ServerAddressTable getLocalConfig() {
		return ConfigBuilder.buildConfigFromFileName("server_address.conf", ServerAddressTable.class);
	}

}
