package com.mokylin.bleach.test.dataserver;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.mokylin.bleach.core.concurrent.process.ProcessUnit;
import com.mokylin.bleach.core.concurrent.process.ProcessUnitHelper;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.function.Function0;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.RedisService;
import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.bleach.dataserver.conf.DataServerConfig;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.test.common.util.MultiThread;

/**
 * 测试基类，负责一些公共工具的初始化
 *
 */
public class AbstractTest {
	
	RedisService redisService = new RedisService(null);

	IRedis redis;
	
	Process run;
	
	HibernateDBService dbService;
	
	Mapping mappingConf;
	
	ProcessUnit LOGIN = ProcessUnitHelper.createSingleProcessUnit("Login Process Unit");
	
	@Before
	public void config() throws Exception{
		//启动Redis
		Future<Void> future = MultiThread.UTIL.asyncExecute(new Function0<Void>() {

			@Override
			public Void apply() {
				try {
					File dir = new File("redis/redis-server.exe");
					run = Runtime.getRuntime().exec(dir.getAbsolutePath());
					run.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				return null;
			}
		});
		
		//初始化配置文件
		mappingConf = ConfigBuilder.buildConfigFromFileName("dataserver/MappingTest.conf", Mapping.class);

		//初始化数据库
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource("h2_hibernate_test.cfg.xml");
		String[] dbResources = new String[] { "hibernate_query.xml" };
		dbService = new HibernateDBService("com.mokylin.bleach.gamedb.orm.entity", new Properties(), url, dbResources);
		
		boolean redisStarted = false;
		try{
			future.get(5, TimeUnit.SECONDS);//等待redis启动
		}catch(TimeoutException e){
			redisStarted = true;
		}
		
		if(!redisStarted){
			Assert.fail("Test Redis can not started!");
			throw new RuntimeException("Test Redis can not started!");
		}
		redisService.addNewRedisConnection(new RedisConfig("default" , "127.0.0.1", 6379, 6), null);
		redis = redisService.getRedis("default").get();
		
		//将MySQL替换为H2
		DataServerConfig oldConfig = DataServerConfig.getDataServerConfig();
		DataServerConfig dsc = new DataServerConfig(oldConfig.serverConfig, oldConfig.sqlProperties, mappingConf);
		Class<Globals> globalsClassType = Globals.class;
		Field dbserviceField = globalsClassType.getDeclaredField("dbservice");
		dbserviceField.setAccessible(true);
		dbserviceField.set(null, this.dbService);
		dbserviceField.setAccessible(false);
		//替换配置文件
		Field mappingConfField = globalsClassType.getDeclaredField("serverConfig");
		mappingConfField.setAccessible(true);
		mappingConfField.set(null, dsc);
		mappingConfField.setAccessible(false);
	}
	
	@After
	public void bye(){
		run.destroy();
	}
}
