package com.mokylin.td.clientmsg.core;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.transaction.NotSupportedException;

/**
 * 消息接口
 * <p>用于与客户端通讯
 * @author baoliang.shen
 *
 */
public interface ICommunicationDataBase extends ISerializationDataBase {

	/**
	 * 
	 * @return 消息号
	 */
	public int getSerializationID();
}
