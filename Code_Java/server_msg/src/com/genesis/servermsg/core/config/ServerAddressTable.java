package com.genesis.servermsg.core.config;

import com.genesis.core.config.ConfigBuilder;
import com.genesis.servermsg.core.config.model.ServerInfo;
import com.genesis.servermsg.core.isc.ServerType;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServerAddressTable {

    private Table<ServerType, Integer, ServerInfo> table;

    public ServerAddressTable() {
        this(HashBasedTable.<ServerType, Integer, ServerInfo>create());
    }

    public ServerAddressTable(Table<ServerType, Integer, ServerInfo> table) {
        this.table = checkNotNull(table);
    }

    public static ServerAddressTable getLocalConfig() {
        return ConfigBuilder
                .buildConfigFromFileName("server_address.conf", ServerAddressTable.class);
    }

    public Table<ServerType, Integer, ServerInfo> getTable() {
        return table;
    }

    public void setTable(Table<ServerType, Integer, ServerInfo> table) {
        this.table = table;
    }

}
