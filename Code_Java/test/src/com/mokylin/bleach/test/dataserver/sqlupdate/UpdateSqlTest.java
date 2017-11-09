package com.mokylin.bleach.test.dataserver.sqlupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.reflect.PrivateUtil;
import com.mokylin.bleach.dataserver.sqlupdate.SqlUpdate;

/**
 * 数据库版本更新测试
 * @author baoliang.shen
 *
 */
public class UpdateSqlTest{
	
	HibernateDBService dbService;

	@Test
	public void update_map_should_be_right() throws IOException {
		//1.1正常更新——增量
		TreeSet<Integer> treeSet = Sets.newTreeSet(Lists.newArrayList(1,2,3));
		TreeMap<Integer, Node> treeMap = new TreeMap<>();
		treeMap.put(1, new Node(1,null));
		treeMap.put(2, new Node(2,null));
		treeMap.put(3, new Node(3,null));
		treeMap.put(4, new Node(4,null));
		treeMap.put(5, new Node(5,null));
		{
			Object temp = PrivateUtil.invoke(new SqlUpdate(), "getUpdateMap",
					new Class[] { treeSet.getClass(), treeMap.getClass()}, new Object[] { treeSet, treeMap });
			@SuppressWarnings("unchecked")
			TreeMap<Integer, Node> updateMap = (TreeMap<Integer, Node>) temp;
			assertThat(updateMap.size(), is(2));
			assertThat(updateMap.firstKey(), is(4));
			assertThat(updateMap.lastKey(), is(5));
		}
		
		//1.2正常更新——从无到有
		{
			treeSet.clear();
			Object temp = PrivateUtil.invoke(new SqlUpdate(), "getUpdateMap",
					new Class[] { treeSet.getClass(), treeMap.getClass()}, new Object[] { treeSet, treeMap });
			@SuppressWarnings("unchecked")
			TreeMap<Integer, Node> updateMap = (TreeMap<Integer, Node>) temp;
			assertThat(updateMap.size(), is(5));
			assertThat(updateMap.firstKey(), is(1));
			assertThat(updateMap.lastKey(), is(5));
		}
		
		//2.1非正常更新——已更过的版本有缺失
		{
			treeSet.add(1);
			treeSet.add(3);
			try {
				Object temp = PrivateUtil.invoke(new SqlUpdate(), "getUpdateMap",
						new Class[] { treeSet.getClass(), treeMap.getClass()}, new Object[] { treeSet, treeMap });
				@SuppressWarnings("unchecked")
				TreeMap<Integer, Node> updateMap = (TreeMap<Integer, Node>) temp;
				assertThat(1, is(2));
			} catch (Exception e) {
				//出异常就正确了
			}
		}
		
		//2.2非正常更新——已更过的版本不存在
		{
			treeSet.clear();
			treeSet.add(0);
			treeSet.add(1);
			treeSet.add(3);
			try {
				Object temp = PrivateUtil.invoke(new SqlUpdate(), "getUpdateMap",
						new Class[] { treeSet.getClass(), treeMap.getClass()}, new Object[] { treeSet, treeMap });
				@SuppressWarnings("unchecked")
				TreeMap<Integer, Node> updateMap = (TreeMap<Integer, Node>) temp;
				assertThat(1, is(2));
			} catch (Exception e) {
				//出异常就正确了
			}
		}
	}
	
	class Node {
		int version;
		File file;
		
		public Node(int version, File file) {
			this.version = version;
			this.file = file;
		}
	}
}
