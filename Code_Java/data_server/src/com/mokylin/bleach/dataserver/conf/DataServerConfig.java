package com.mokylin.bleach.dataserver.conf;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.ServerType;
import com.typesafe.config.Config;

public class DataServerConfig {

	public final ServerConfig serverConfig;
	
	public final SqlProperties sqlProperties;
	
	public final Mapping mappingConf;
	
	public DataServerConfig(ServerConfig sConfig, SqlProperties sqlProperties, Mapping mappingConf){
		checkArgument(sqlProperties!=null && sqlProperties.check());
		this.serverConfig = checkNotNull(sConfig);
		this.sqlProperties = sqlProperties;
		this.mappingConf = checkNotNull(mappingConf);
	}
	
	private static Config config = ConfigBuilder.buildConfigFromFileName("DataServer.conf");
	
	public static AkkaConfig getAkkaConfig(){
		return new AkkaConfig(config.getConfig("dataServer.akka"));
	}

	public static DataServerConfig getDataServerConfig() {
		return new DataServerConfig(new ServerConfig(ServerType.DATA_SERVER, config.getInt("dataServer.serverId"), getAkkaConfig()), getSqlProperties(), getMappingConf());
	}

	private static Mapping getMappingConf() {
		return ConfigBuilder.buildConfigFromFileName("Mapping.conf", Mapping.class);
	}

	private static SqlProperties getSqlProperties() {
		Config subConfig = config.getConfig("dataServer.database");
		SqlProperties sqlProperties = new SqlProperties(subConfig.getString("ip"), subConfig.getInt("port"), subConfig.getString("databaseName"), subConfig.getString("username"), subConfig.getString("password"));
		return sqlProperties;
	}
}
