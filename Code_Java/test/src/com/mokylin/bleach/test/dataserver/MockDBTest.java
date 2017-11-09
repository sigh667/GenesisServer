package com.mokylin.bleach.test.dataserver;

import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.test.dataserver.gamedb.HumanTable;

public class MockDBTest {

	@Test
	public void test() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource("h2_hibernate_test.cfg.xml");
		
		String[] dbResources = new String[] { "hibernate_query.xml" };
		HibernateDBService dbService = new HibernateDBService("com.mokylin.bleach.gamedb.orm.entity", new Properties(), url, dbResources);
		
		new HumanTable().insertToDB();
		
		HumanEntity he = dbService.getById(HumanEntity.class, 1L);
		System.out.println(he.getName());
	}
	

}
