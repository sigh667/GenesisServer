package com.mokylin.td.clientmsg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.io.UnsupportedEncodingException;

import javax.transaction.NotSupportedException;

import org.junit.Test;

import com.mokylin.td.clientmsg.core.SerializationHelper;
import com.mokylin.td.clientmsg.enumeration.EnumLoginFailReason;

public class SerializationHelperTest {

	@Test
	public void test_add_and_pop_custom_enum() throws UnsupportedEncodingException, NotSupportedException, InstantiationException, IllegalAccessException {
		EnumLoginFailReason[] values = EnumLoginFailReason.values();
		ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();

		for (EnumLoginFailReason enumLoginFailReason : values) {
			SerializationHelper.customSerialization(buffer, enumLoginFailReason);
		}
		for (int i = 0; i < values.length; i++) {
			Object oo = SerializationHelper.customDeserialization(buffer, EnumLoginFailReason.class);
			assertThat(oo instanceof EnumLoginFailReason, is(true));
			assertThat(values[i].equals(oo), is(true));
		}
	}

	@Test
	public void test_add_and_pop_UTF() throws UnsupportedEncodingException {
		ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
		String str1 = "试试中文";
		SerializationHelper.writeUTF(buffer, str1);
		String _str1 = SerializationHelper.readUTF(buffer);
		assertThat(_str1.equals(str1), is(true));
	}
}
