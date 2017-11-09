package com.mokylin.bleach.test.core.config;

import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.exception.ConfigBuildException;
import com.mokylin.bleach.test.core.config.mock.MapConfig;
import com.mokylin.bleach.test.core.config.mock.TableConfig;
import com.mokylin.bleach.test.core.config.mock.TestClass;
import com.mokylin.bleach.test.core.config.mock.TestEnum;
import com.mokylin.bleach.test.core.config.serverconfig.GameServerConfig;

/**
 * Tests for ConfigBuilder.
 * @author yaguang.xiao
 *
 */
@RunWith(JUnit4.class)
public class ConfigBuilderTest {
	
	@Test
	public void config_object_should_have_the_same_content_with_config_file() {
		GameServerConfig config = ConfigBuilder.buildConfigFromFileName("config/server.conf", GameServerConfig.class);
		
		Assert.assertThat(config.getServerName(), is("Server2"));
		Assert.assertThat(config.getHost(), is("127.0.0.3"));
		Assert.assertThat(config.getPort(), is(2256));
		Assert.assertThat(config.getLogConfig().getLogServerIp(), is("127.0.0.4"));
		Assert.assertThat(config.getLogConfig().getLogServerPort(), is(2256));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo1(), is(1));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo2(), is("this is info2"));
	}
	
	@Test
	public void all_field_in_config_object_should_have_the_default_value() {
		GameServerConfig config = ConfigBuilder.buildConfigFromFileName("config/server2.conf", GameServerConfig.class);
		
		Assert.assertThat(config.getServerName(), is(""));
		Assert.assertThat(config.getHost(), is(""));
		Assert.assertThat(config.getPort(), is(2233));
		Assert.assertThat(config.getLogConfig().getLogServerIp(), is("127.0.0.1"));
		Assert.assertThat(config.getLogConfig().getLogServerPort(), is(8800));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo1(), is(23));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo2(), is("info2"));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void config_file_should_not_be_found() {
		thrown.expect(ConfigBuildException.class);
		thrown.expectMessage("configuration file path is not valid!");
		
		ConfigBuilder.buildConfigFromFileName("config/invalid.conf", GameServerConfig.class);
	}
	
	@Test
	public void config_file_content_should_be_invalid() {
		thrown.expect(ConfigBuildException.class);
		thrown.expectMessage("parse configuration file content fail!please check your configuration file's format!");
		
		ConfigBuilder.buildConfigFromFileName("config/invalidContent.conf", GameServerConfig.class);
	}
	
	@Test
	public void map_field_should_be_a_map() {
		MapConfig conf = ConfigBuilder.buildConfigFromFileName("config/map.conf", MapConfig.class);
		Map<TestEnum, Integer> map = conf.getMap();
		
		int gsInt = map.get(TestEnum.GS);
		int dbsInt = map.get(TestEnum.DBS);
		
		Assert.assertThat(gsInt, is(100));
		Assert.assertThat(dbsInt, is(101));
	}
	
	@Test
	public void table_field_should_be_a_table() {
		TableConfig conf = ConfigBuilder.buildConfigFromFileName("config/table.conf", TableConfig.class);
		Table<TestEnum, Integer, TestClass> table = conf.getTable();
		
		int serverId = table.get(TestEnum.GS, 2001).getServerID();
		int port = table.get(TestEnum.DBS, 10086).getPort();
		
		Assert.assertThat(serverId, is(2001));
		Assert.assertThat(port, is(10086));
	}
}
