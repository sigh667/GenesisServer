package com.mokylin.bleach.gameserver.player.exception;

/**
 * 玩家初始化异常。<p>
 * 
 * 该异常表示玩家的初始化失败。
 * 
 * @author pangchong
 *
 */
public class PlayerInitException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PlayerInitException(String msg){
		super(msg);
	}

}
