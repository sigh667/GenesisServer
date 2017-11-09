package com.mokylin.bleach.dataserver.sqlupdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Charsets;
import com.mokylin.bleach.core.file.FileViewer;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.util.IOUtils;
import com.mokylin.bleach.dataserver.conf.DataServerConfig;
import com.mokylin.bleach.gamedb.orm.entity.DbVersion;

/**
 * 数据库的自动更新功能
 * @author baoliang.shen
 *
 */
public class SqlUpdate {

	private static final int dbInitVersionId = 19700101;
	private static final String dbInitFileName = dbInitVersionId + ".sql";

	/**创建bleach库*/
	public static final String createSchemaIfNotExists = "CREATE SCHEMA IF NOT EXISTS bleach DEFAULT CHARACTER SET utf8;";
	/**创建t_db_version表*/
	public static final String createDbVersionIfNotExists = "CREATE TABLE IF NOT EXISTS `t_db_version` (" +
			"`id` int(11) NOT NULL COMMENT '版本号'," +
			"`updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '版本更新时间'," +
			"PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	/**
	 * 创建只用于更新数据库的dbservice，并且对表结构设置为不检查
	 * @param serverConfig 
	 * @return
	 */
	public static HibernateDBService buildUpdateOnlyDBService(DataServerConfig serverConfig) {
		Properties properties = serverConfig.sqlProperties.getConfigProperties(true);
		properties.setProperty("hibernate.hbm2ddl.auto", "none");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL _dbUrl = classLoader.getResource("hibernate.cfg.xml");
		String[] _dbResources = new String[] { "hibernate_query.xml" };

		return new HibernateDBService("com.mokylin.bleach.gamedb.orm.entity", properties, _dbUrl, _dbResources);
	}

	/**
	 * 更新数据库
	 * @param dbservice
	 * @throws IOException 
	 */
	public static void updateSQL(DataServerConfig serverConfig, HibernateDBService dbservice) throws IOException {
		//1.0如果库不存在，就创建它
		dbservice.doSqlQueryForUpdate(SqlUpdate.createSchemaIfNotExists);
		
		dbservice.doSqlQueryForUpdate("use " + serverConfig.sqlProperties.getDatabaseName());

		//2.0如果版本表不存在，就创建它
		dbservice.doSqlQueryForUpdate(SqlUpdate.createDbVersionIfNotExists);

		//3.0取出数据库中标记的所有版本号
		TreeSet<Integer> treeSet = queryDbVersions(dbservice);

		//4.0找出本地的所有版本号及其SQL文
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL rr = classLoader.getResource(dbInitFileName);
		String db_initPath = rr.getPath();
		final int endIndex = db_initPath.lastIndexOf(dbInitFileName);
		String folderPath = db_initPath.substring(0, endIndex);

		File fileInit = new File(rr.getPath());
		if (!fileInit.isFile()) {
			throw new RuntimeException(dbInitFileName + "竟然不存在！");
		}

		List<String> sqlFileList = FileViewer.getListFiles(folderPath, "sql", true);
		if (sqlFileList==null || sqlFileList.isEmpty()) {
			throw new RuntimeException("本地竟然连一个SQL文都没有！");
		}
		TreeMap<Integer, Node> treeMap = new TreeMap<>();
		for (String filePath : sqlFileList) {
			File file = new File(filePath);
			if (!file.isFile()) {
				throw new RuntimeException(filePath + "竟然不是一个文件！应该是FileViewer.getListFiles方法有问题");
			}

			String str = file.getName();
			final int tempIndex = str.lastIndexOf(".sql");
			if (tempIndex==-1) {
				throw new RuntimeException(filePath + "的后缀名竟然不是.sql！应该是FileViewer.getListFiles方法有问题");
			}
			String numStr = str.substring(0, tempIndex);

			Node node = new Node();
			node.file = file;
			final Integer versionId = Integer.parseInt(numStr);
			node.version = versionId;

			treeMap.put(versionId, node);
		}

		TreeMap<Integer, Node> updateTreeMap = getUpdateMap(treeSet, treeMap);
		if (updateTreeMap!=null && !updateTreeMap.isEmpty()) {
			doUpdate(dbservice, updateTreeMap);
		}
	}

	private static TreeMap<Integer, Node> getUpdateMap(TreeSet<Integer> treeSet, TreeMap<Integer, Node> treeMap) {
		TreeMap<Integer, Node> updateTreeMap = null;
		if (treeSet.isEmpty()) {
			updateTreeMap = treeMap;
		} else {
			//检查过往更新过的版本号
			final int fromKey = treeSet.first();
			final int toKey = treeSet.last();
			NavigableMap<Integer, Node> sameMap = treeMap.subMap(fromKey, true, toKey, true);
			if (sameMap.size()>treeSet.size()) {
				throw new RuntimeException("有过往的SQL文未被执行");
			} else if (sameMap.size()<treeSet.size()) {
				throw new RuntimeException("执行过的SQL文比现存的要多");
			}
			for (Integer id : sameMap.keySet()) {
				if (!treeSet.contains(id)) {
					throw new RuntimeException(id + "号版本SQL未被执行过");
				}
			}

			//计算出本次需要更新的版本
			NavigableMap<Integer, Node> updateMap = treeMap.tailMap(toKey, false);
			if (updateMap!=null && !updateMap.isEmpty()) {
				updateTreeMap = new TreeMap<>(updateMap);
			}
		}
		return updateTreeMap;
	}

	/**
	 * 取出数据库中所有版本号
	 * @param dbservice
	 * @return
	 */
	private static TreeSet<Integer> queryDbVersions(HibernateDBService dbservice) {
		List<DbVersion> versionListInSql = dbservice.getAll(DbVersion.class);
		TreeSet<Integer> treeSet = new TreeSet<>();
		if (versionListInSql!=null && !versionListInSql.isEmpty()) {
			for (DbVersion dbVersion : versionListInSql) {
				final int id = dbVersion.getId();
				treeSet.add(id);
			}
		}
		return treeSet;
	}

	/**
	 * 执行更新操作
	 * @param dbservice 
	 * @param treeMap	本次要执行的SQL
	 * @throws IOException 
	 */
	private static void doUpdate(HibernateDBService dbservice, TreeMap<Integer, Node> treeMap) throws IOException {
		//执行本次更新的SQL
		for (Node node : treeMap.values()) {
			doOneUpdate(dbservice, node);
		}
	}

	private static void doOneUpdate(HibernateDBService dbservice, Node node) throws FileNotFoundException, IOException {

		InputStream in = new FileInputStream(node.file);
		String content = null;
		content = IOUtils.readAndClose(in, Charsets.UTF_8.name());
		if (content!=null && !content.isEmpty()) {
			String[] strs = content.split(";");
			for (String string : strs) {
				if (StringUtils.isBlank(string))
					continue;
				
				dbservice.doSqlQueryForUpdate(string);
			}
		}
		
		String queryContent = String.format("insert into t_db_version (id) values (%d)", node.version);
		dbservice.doSqlQueryForUpdate(queryContent );
	}
}
