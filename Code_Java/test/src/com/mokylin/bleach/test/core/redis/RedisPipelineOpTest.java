package com.mokylin.bleach.test.core.redis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;

public class RedisPipelineOpTest extends AbstractOpTest{

	@Test
	public void pipeline_should_act_right_way() {
		final String key = UUID.randomUUID().toString();
		final String valueKey = key + ":value";
		final String listKey = key + ":list";
		final String setKey = key + ":set";
		final String hashKey = key + ":hash";
		IPipelineOp pipeline = redis.pipeline();
		IRedisResponse<List<IRedisResponse<?>>> results = pipeline.exec(new PipelineProcess() {
			@Override
			public void apply() {
				this.getValueOp().set(valueKey, valueKey);
				this.getListOp().lpush(listKey, listKey);
				this.getSetOp().sadd(setKey, setKey);
				this.getHashOp().hset(hashKey, hashKey, hashKey);
			}
		});
		
		assertThat(redis.getValueOp().get(valueKey, String.class).get(), is(valueKey));
		assertThat(redis.getListOp().lpop(listKey, String.class).get(), is(listKey));
		assertThat(redis.getHashOp().hget(hashKey, hashKey, String.class).get(), is(hashKey));
		assertThat(redis.getSetOp().spop(setKey, String.class).get(), is(setKey));
		assertThat(results.isSuccess(), is(true));
		
		List<IRedisResponse<?>> lists = results.get();
		assertThat((String)lists.get(0).get(), is("OK"));
		assertThat((Long)lists.get(1).get(), is(1L));
		assertThat((Long)lists.get(2).get(), is(1L));
		assertThat((Long)lists.get(3).get(), is(1L));
	}

}
