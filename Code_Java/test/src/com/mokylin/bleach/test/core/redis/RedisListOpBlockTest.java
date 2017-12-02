package com.mokylin.bleach.test.core.redis;

import com.mokylin.bleach.core.function.Function0;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.test.common.util.MultiThread;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class RedisListOpBlockTest extends AbstractOpTest{
    	@Test
    	public void brpop_should_block_and_get_value(){
    		final String key = UUID.randomUUID().toString();
    		Future<MockEntity> result = MultiThread.UTIL.asyncExecute(new Function0<MockEntity>() {
    			@Override
    			public MockEntity apply() {
                    IRedisResponse<MockEntity> iRedisResponse =
                            redis.getListOp().brpop(key, MockEntity.class);
                    return iRedisResponse.get();
    			}
    		});

            MockEntity entityA = null;
            try {
    			entityA = result.get(1, TimeUnit.SECONDS);
                Assert.fail();
            } catch (Exception e) {
                assertThat(entityA, is(nullValue()));
            }

    		redis.getListOp().lpush(key, makeMockEntity());
    		try {
    			MockEntity entity = result.get(10, TimeUnit.SECONDS);
    			assertThat(entity.getExp(), is(13568));
    			assertThat(entity.getId(), is(133450909L));
    			assertThat(entity.getName(), is("这是什么"));
    			assertThat(entity.getJapaneseName(), is("くろさきいちご"));
    		} catch (Exception e) {
    			Assert.fail();
    		}
    	}

    	@Test
    	public void lrpop_should_block_and_get_value(){
    		final String key = UUID.randomUUID().toString();
    		Future<MockEntity> result = MultiThread.UTIL.asyncExecute(new Function0<MockEntity>() {
    			@Override
    			public MockEntity apply() {
    				return redis.getListOp().blpop(key, MockEntity.class).get();
    			}
    		});

    		try {
    			MockEntity entity = result.get(1, TimeUnit.SECONDS);
    			assertThat(entity, is(nullValue()));
    			Assert.fail();
    		} catch (Exception e) {
    		}

    		redis.getListOp().lpush(key, makeMockEntity());
    		try {
    			MockEntity entity = result.get(10, TimeUnit.SECONDS);
    			assertThat(entity.getExp(), is(13568));
    			assertThat(entity.getId(), is(133450909L));
    			assertThat(entity.getName(), is("这是什么"));
    			assertThat(entity.getJapaneseName(), is("くろさきいちご"));
    		} catch (Exception e) {
    			Assert.fail();
    		}
    	}
}
