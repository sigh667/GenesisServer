package com.mokylin.td.clientmsg.core;

import java.io.UnsupportedEncodingException;

import javax.transaction.NotSupportedException;

import io.netty.buffer.ByteBuf;

public interface ICommunicationDataDefine {

	public ICommunicationDataBase getCommunicationData(ByteBuf __bytes, int __messageID)
			throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, NotSupportedException;
}
