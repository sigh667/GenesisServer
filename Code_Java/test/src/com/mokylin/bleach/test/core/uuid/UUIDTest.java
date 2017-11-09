package com.mokylin.bleach.test.core.uuid;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Test;

import com.mokylin.bleach.core.uuid.IUUIDGenerator;
import com.mokylin.bleach.core.uuid.UUIDGeneratorFactory;
import com.mokylin.bleach.core.uuid.UUIDHelper;
import com.mokylin.bleach.core.uuid.helper.Scope;

/**
 * UUID测试程序
 * @author yaguang.xiao
 *
 */
public class UUIDTest {
	
	@Test
	public void uuid_generator_should_generate_sequential_id_from_100() {
		short channelId = 0;
		int serverId = 0;
		
		IUUIDGenerator uuidGenerator = UUIDGeneratorFactory.createUUIDGenerator(channelId, serverId, TestType.class, null);
		long uuid1 = uuidGenerator.getNextUUID(TestType.Test);
		long uuid2 = uuidGenerator.getNextUUID(TestType.Test);
		long uuid3 = uuidGenerator.getNextUUID(TestType.Test);
		long uuid4 = uuidGenerator.getNextUUID(TestType.Test);
		long uuid5 = uuidGenerator.getNextUUID(TestType.Test);
		Assert.assertArrayEquals(new long[] {101, 102, 103, 104, 105}, new long[] {uuid1, uuid2, uuid3, uuid4, uuid5});
	}
	
	@Test
	public void the_uuid_scope_must_be_correct() {
		Scope scope = UUIDHelper.getScope(0, 0);
		
		Assert.assertThat(scope.minValue, is(0l));
		Assert.assertThat(scope.maxValue, is((long)(Math.pow(2, 38) - 1)));
	}
	
}
