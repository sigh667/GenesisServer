package com.mokylin.bleach.common.combat.enumeration;

/**
 * 目标选择方式
 * @author baoliang.shen
 *
 */
public enum TargetSelectorType {

	YouFangMoRenMuBiao,			//友方默认目标
	DiFangMoRenMuBiao,			//敌方默认目标
	YouFangQuanYuanSuiJi,		//友方全员中随机
	YouFangChuZiJiWaiSuiJi,		//友方除自己外随机
	DiFangQuanYuanSuiJi,		//敌方全员中随机
	QuanChangChuZiJiWaiSuiJi,	//全场除自身外随机
	QuanChangSuiJi,				//全场随机
	YouFangLowestHP,			//友方HP最少
	DiFangHighestHP,			//敌方HP最多
	DiFangLowestHP,				//敌方HP最少
	DiFangNextXingDong,			//下一个行动
	DiFangHouPaiJuLiZuiJin,		//敌方距离后排最近
	DiFangHighestLiLiang,		//敌方力量最高
	DiFangLowestFangYu,			//敌方防御最低
	DiFangLowestZhuanJing,		//敌方专精最低
	;
}
