package com.mokylin.bleach.test.core.redis;

import com.mokylin.bleach.core.redis.op.IListOp;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RedisListOpTest extends AbstractOpTest {
	
	@Test
	public void listOp_should_lpush_and_pop_value_successful() {
		String key = UUID.randomUUID().toString();
		IListOp listOp = redis.getListOp();
		
		MockEntity entity = this.makeMockEntity();
		entity.setId(1);
		listOp.lpush(key, entity);
		entity.setId(2);
		listOp.lpush(key, entity);
		entity.setId(3);
		listOp.lpush(key, entity);
		
		MockEntity tail = listOp.rpop(key, MockEntity.class).get();
		MockEntity head = listOp.lpop(key, MockEntity.class).get();
		assertThat(tail.getId(), is(1L));
		assertThat(head.getId(), is(3L));
	}
	
	@Test
	public void listOp_should_rpush_and_pop_value_successful() {
		String key = UUID.randomUUID().toString();
		IListOp listOp = redis.getListOp();
		
		MockEntity entity = this.makeMockEntity();
		entity.setId(1);
		listOp.rpush(key, entity);
		entity.setId(2);
		listOp.rpush(key, entity);
		entity.setId(3);
		listOp.rpush(key, entity);
		
		MockEntity tail = listOp.rpop(key, MockEntity.class).get();
		MockEntity head = listOp.lpop(key, MockEntity.class).get();
		assertThat(tail.getId(), is(3L));
		assertThat(head.getId(), is(1L));
	}
	
	@Test
	public void listOp_should_lpush_and_rpush_act_right_way() {
		String key = UUID.randomUUID().toString();
		IListOp listOp = redis.getListOp();
		
		MockEntity entity = this.makeMockEntity();
		entity.setId(1);
		listOp.lpush(key, entity);
		entity.setId(2);
		listOp.rpush(key, entity);
		entity.setId(3);
		listOp.rpush(key, entity);
		entity.setId(4);
		listOp.lpush(key, entity);
		
		MockEntity tail = listOp.rpop(key, MockEntity.class).get();
		MockEntity head = listOp.lpop(key, MockEntity.class).get();
		assertThat(tail.getId(), is(3L));
		assertThat(head.getId(), is(4L));
	}
	
	@Test
	public void listOp_should_lpush_and_rpush_multivalue_act_right_way() {
		String key = UUID.randomUUID().toString();
		IListOp listOp = redis.getListOp();
		
		
		
		MockEntity entity1 = this.makeMockEntity();
		entity1.setId(1);
		System.out.println(redis.isConnected());
		MockEntity entity2 = this.makeMockEntity();
		entity2.setId(2);
		listOp.lpush(key, entity1, entity2);
		MockEntity entity3 = this.makeMockEntity();
		entity3.setId(3);
		MockEntity entity4 = this.makeMockEntity();
		entity4.setId(4);
		listOp.rpush(key, entity3, entity4);
		
		MockEntity tail = listOp.rpop(key, MockEntity.class).get();
		MockEntity head = listOp.lpop(key, MockEntity.class).get();
		assertThat(tail.getId(), is(4L));
		assertThat(head.getId(), is(2L));
	}
}
