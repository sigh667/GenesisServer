package com.mokylin.bleach.core.isc.msg;

/**
 * 用于获取远程ActorRef的消息。<p>
 * 
 * 当向远程的Actor发送该消息时，会收到远程整体ActorRef的组装类，
 * 将该组装类保存在本地，可以用于之后和远程Actor通信。
 * 
 * @author pangchong
 *
 */
public enum Connect { INSTANCE }
