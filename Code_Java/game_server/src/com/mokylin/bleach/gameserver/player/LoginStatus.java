package com.mokylin.bleach.gameserver.player;

import com.mokylin.bleach.protobuf.MessageType;
import com.mokylin.bleach.protobuf.MessageType.CGMessageType;

/**
 * 玩家的登录状态
 * @author baoliang.shen
 *
 */
public enum LoginStatus {
	/**初始状态*/
	Init {
		@Override
		public boolean isCanProcess(int messageType) {
			if (CGMessageType.CG_LOGIN_VALUE==messageType) {
				return true;
			}
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			// Nothing to do
		}
	},
	/**认证中*/
	Authing {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			player.setStatus(Logouting);
		}
	},
	
	AuthFailed{
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			playerManagerArgs.loginService.removePlayer(player.getChannel(), player.getAccountId());
		}
	},
	/**已认证*/
	Authed {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
		}
	},
	/**等待客户端创建角色*/
	CreatingHuman {
		@Override
		public boolean isCanProcess(int messageType) {
			if (CGMessageType.CG_CREATE_ROLE_VALUE==messageType) {
				return true;
			}
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
		}
	},
	/**等待客户端选择角色*/
	SelectingHuman {
		@Override
		public boolean isCanProcess(int messageType) {
			if (CGMessageType.CG_SELECT_ROLE_VALUE==messageType) {
				return true;
			}
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
		}
	},
	/**加载角色中*/
	LoadingHuman {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			player.setStatus(Logouting);
		}
	},
	/**加载角色发生异常*/
	LoadingHumanFailed{

		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
		}
	},
	/**加载角色成功*/
	LoadingHumanSucceed {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
		}
	},
	/**等待持久化完毕，以进行角色加载*/
	WaitingPersistance {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			player.setStatus(Logouting);
		}
	},
	/**游戏中*/
	Gaming {
		@Override
		public boolean isCanProcess(int messageType) {
			if (messageType>=MessageType.CGMessageType.CG_LOGIN_MSG_BEGIN_VALUE
					&& messageType<=MessageType.CGMessageType.CG_LOGIN_MSG_END_VALUE) {
				return false;
			}
			return true;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			//设置状态
			player.setStatus(LoginStatus.Logouting);
		}
	},
	/**退出中*/
	Logouting {
		@Override
		public boolean isCanProcess(int messageType) {
			return false;
		}

		@Override
		public void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs) {
			// Nothing to do
		}
	},
	;

	/**
	 * 当前状态是否可以处理此消息
	 * @param messageType	消息号
	 * @return
	 */
	public abstract boolean isCanProcess(int messageType);
	/**
	 * 在Login线程中开始执行下线操作
	 * @param player	要下线的玩家
	 */
	public abstract void logoutInPlayerManagerActor(Player player, PlayerManagerArgs playerManagerArgs);
}
