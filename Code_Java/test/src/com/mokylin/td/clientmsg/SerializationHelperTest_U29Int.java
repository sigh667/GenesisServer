package com.mokylin.td.clientmsg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import org.junit.Test;

import com.mokylin.td.clientmsg.core.SerializationHelper;

/**
 * {@link SerializationHelper}的单元测试
 * @author baoliang.shen
 *
 */
public class SerializationHelperTest_U29Int {

	/**
	 * 本测试需要225秒以上的时间
	 */
	@Test
	public void test_add_and_pop() {
		
		int max = SerializationHelper.maxU29Int;	//2^28次方 - 1
		int min = SerializationHelper.minU29Int;	//-(2^28次方)
		ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
		for (int i = min; i <= max; i++) {
			SerializationHelper.writeU29Int(buffer, i);
			int readU29Int = SerializationHelper.readU29Int(buffer);
			assertThat(readU29Int, is(i));
		}
	}
}
