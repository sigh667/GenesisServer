package com.mokylin.bleach.robot.ui.loading;


/**
 * 模态阻挡框的类型
 * @author baoliang.shen
 *
 */
public enum LoadingDlgType {
	/**登陆中*/
	Logining {

		@Override
		public String getTitle() {
			return "登录认证中";
		}
	},
	/**创建角色*/
	CreatingRole {

		@Override
		public String getTitle() {
			return "正在创建角色";
		}
	},
	/**选择角色*/
	SelectingRole {
		@Override
		public String getTitle() {
			return "正在选择角色";
		}
	},
	;

	public abstract String getTitle();
}
