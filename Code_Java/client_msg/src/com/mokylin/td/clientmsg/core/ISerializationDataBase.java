package com.mokylin.td.clientmsg.core;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import javax.transaction.NotSupportedException;

public interface ISerializationDataBase {

	/**
	 * 序列化
	 * @param __targetBytes
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NotSupportedException 
	 */
	public ByteBuf toBytes(ByteBuf __targetBytes) throws UnsupportedEncodingException, NotSupportedException;

	/**
	 * 反序列化
	 * @param __serializationBytes
	 * @throws UnsupportedEncodingException 
	 * @throws NotSupportedException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void fromBytes(ByteBuf __serializationBytes) throws UnsupportedEncodingException, NotSupportedException, InstantiationException, IllegalAccessException;

}
