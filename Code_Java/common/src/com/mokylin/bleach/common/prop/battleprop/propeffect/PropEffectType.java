package com.mokylin.bleach.common.prop.battleprop.propeffect;

/**
 * 属性作用类型
 * @author yaguang.xiao
 *
 */
public enum PropEffectType {

	/** 绝对值作用 */
	Abs() {

		@Override
		public String getSymbol() {
			return "+";
		}
		
	},
	/** 百分比作用 */
	Per() {

		@Override
		public String getSymbol() {
			return "%";
		}
		
	}
	;
	
	/**
	 * 获取标识本属性作用类型的符号
	 * @return
	 */
	public abstract String getSymbol();
}
