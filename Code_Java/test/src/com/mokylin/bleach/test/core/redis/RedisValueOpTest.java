package com.mokylin.bleach.test.core.redis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IValueOp;

public class RedisValueOpTest extends AbstractOpTest {
	
	static final String key = "hello";
	
	@Test
	public void valueop_should_set_and_get_key_from_redis() {
		MockEntity me = makeMockEntity();
		IValueOp valueOp = redis.getValueOp();
		valueOp.set(key, me);
		IRedisResponse<MockEntity> result = valueOp.get(key, MockEntity.class);
		if(result.isSuccess()){
			MockEntity value = result.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("这是什么"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(result.errorMsg());
		}		
	}
	
	@Test
	public void set_the_same_key_should_overwrite_exists_key(){
		MockEntity me = makeMockEntity();
		IValueOp valueOp = redis.getValueOp();
		valueOp.set(key, me);
		me.setName("黑崎一护");
		me.setJapaneseName("黒崎一護");
		valueOp.set(key, me);
		IRedisResponse<MockEntity> result = valueOp.get(key, MockEntity.class);
		if(result.isSuccess()){
			MockEntity value = result.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("黑崎一护"));
			assertThat(value.getJapaneseName(), is("黒崎一護"));
		}else{
			Assert.fail(result.errorMsg());
		}	
	}
	
	@Test
	public void setIfAbsent_should_set_when_key_does_not_exist(){
		MockEntity me = makeMockEntity();
		me.setName("setIfAbsent1");
		IValueOp valueOp = redis.getValueOp();
		String randKey = UUID.randomUUID().toString();
		IRedisResponse<String> setResult = valueOp.setIfAbsent(randKey, me);
		assertThat(setResult.get(), is("OK"));
		IRedisResponse<MockEntity> getResult = valueOp.get(randKey, MockEntity.class);
		if(getResult.isSuccess()){
			MockEntity value = getResult.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("setIfAbsent1"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(getResult.errorMsg());
		}
	}
	
	@Test
	public void setIfAbsent_should_not_set_when_key_exists(){
		MockEntity me = makeMockEntity();
		IValueOp valueOp = redis.getValueOp();
		valueOp.setIfAbsent(key, me);
		
		me.setName("setIfAbsent2");		
		IRedisResponse<String> setResult = valueOp.setIfAbsent(key, me);
		assertThat(setResult.get(), isEmptyOrNullString());
		IRedisResponse<MockEntity> getResult = valueOp.get(key, MockEntity.class);
		if(getResult.isSuccess()){
			MockEntity value = getResult.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("这是什么"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(getResult.errorMsg());
		}
	}
	
	@Test
	public void setIfPresent_should_set_when_key_exists(){
		MockEntity me = makeMockEntity();
		me.setName("setIfPresent1");
		IValueOp valueOp = redis.getValueOp();
		IRedisResponse<String> setResult = valueOp.setIfPresent(key, me);
		assertThat(setResult.get(), is("OK"));
		IRedisResponse<MockEntity> getResult = valueOp.get(key, MockEntity.class);
		if(getResult.isSuccess()){
			MockEntity value = getResult.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("setIfPresent1"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(getResult.errorMsg());
		}
	}
	
	@Test
	public void setIfPresent_should_not_set_when_key_does_not_exist(){
		MockEntity me = makeMockEntity();
		me.setName("setIfPresent2");
		IValueOp valueOp = redis.getValueOp();
		String randKey = UUID.randomUUID().toString();
		IRedisResponse<String> setResult = valueOp.setIfPresent(randKey, me);
		assertThat(setResult.get(), isEmptyOrNullString());
		IRedisResponse<MockEntity> getResult = valueOp.get(randKey, MockEntity.class);
		assertThat(getResult.get(), is(nullValue()));
	}
	
	@Test
	public void setExpiredBySeconds_should_let_key_expired_after_specific_seconds() throws Exception{
		MockEntity me = makeMockEntity();
		me.setName("setExpiredBySeconds");
		IValueOp valueOp = redis.getValueOp();
		String randKey = UUID.randomUUID().toString();
		valueOp.setExpiredBySeconds(randKey, me, 1);
		IRedisResponse<MockEntity> result = valueOp.get(randKey, MockEntity.class);
		if(result.isSuccess()){
			MockEntity value = result.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("setExpiredBySeconds"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(result.errorMsg());
		}
		Thread.sleep(1500);
		result = valueOp.get(randKey, MockEntity.class);
		assertThat(result.get(), is(nullValue()));
	}
	
	@Test
	public void setExpiredByMilliseconds_should_let_key_expired_after_specific_seconds() throws Exception{
		MockEntity me = makeMockEntity();
		me.setName("setExpiredByMilliseconds");
		IValueOp valueOp = redis.getValueOp();
		String randKey = UUID.randomUUID().toString();
		valueOp.setExpiredByMilliseconds(randKey, me, 1000);
		IRedisResponse<MockEntity> result = valueOp.get(randKey, MockEntity.class);
		if(result.isSuccess()){
			MockEntity value = result.get();
			assertThat(value.getExp(), is(13568));
			assertThat(value.getId(), is(133450909L));
			assertThat(value.getName(), is("setExpiredByMilliseconds"));
			assertThat(value.getJapaneseName(), is("くろさきいちご"));
		}else{
			Assert.fail(result.errorMsg());
		}
		Thread.sleep(1500);
		result = valueOp.get(randKey, MockEntity.class);
		assertThat(result.get(), is(nullValue()));
	}
}
