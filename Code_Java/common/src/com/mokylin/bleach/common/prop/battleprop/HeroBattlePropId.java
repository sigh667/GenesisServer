package com.mokylin.bleach.common.prop.battleprop;

import java.util.Map;

import com.mokylin.bleach.common.prop.IProp;
import com.mokylin.bleach.common.prop.PropType;

/**
 * 战斗属性标识
 * 
 * @author yaguang.xiao
 * 
 */
public enum HeroBattlePropId implements IProp {

	//////////////////////////////////////////////////////////////////////////////
	//一级属性（死神中的一级属性只是策划方面的区分，在程序处理上，一二级属性没有区别）
	LiLiang(1, Float.MAX_VALUE),	//力量（斩）
	Lingli(1, Float.MAX_VALUE),		//灵力（技）
	ZhuanJing(1, Float.MAX_VALUE),	//专精（术）
	
	//////////////////////////////////////////////////////////////////////////////
	//二级属性
	MaxHP(1, Float.MAX_VALUE),		//HP上限
	MaxMP,							//MP上限
	MPChuShi,						//MP初始值
	HPHuiFuPerRound,				//HP回复值：每回合自动回复的HP值
	HPHuiFuPerRoundRatio,			//HP回复值：每回合自动回复的HP比例
	MPHuiFuPerRound,				//MP回复值：每回合自动回复的MP值
	MPGongJiHuiFu,					//攻击MP回复值
	MPBeiJiHuiFu,				    //防守MP回复值
	SuDu,							//速度
	
	GongJiLiMax,					//攻击力上限
	GongJiLiMin,					//攻击力下限
	FangYuLi,						//防御力
	DuoChongDaJi,					//多重打击（多重打击‱）
	PoJia,							//破甲（穿透攻击‱）
	PoJiaDiKang,					//破甲抵抗（穿透抵抗‱）
	BaoJi,							//暴击（暴击‱）
	RenXing,						//韧性（韧性‱）
	BaoJiShangHai,					//暴击伤害（暴击伤害提高‱）
	BaoJiJianMian,					//暴伤减免‱
	ShanBi,							//闪避（闪避‱）
	MingZhong,						//命中‱
	GeDang,							//格挡‱（受到伤害时触发招架的概率）
	GeDangJianMian,					//格挡减免‱（招架能够减免伤害的具体比例）
	ChuanCi,						//穿刺‱（降低被招架概率）
	
	//从三维方向区分的
	PuGongJianMian,					//普通（普攻）伤害减免
	PuGongJiaShen,					//普通（普攻）伤害加深
	JiNengJianMian,					//技能（刀技）伤害减免
	JiNengJiaShen,					//技能（刀技）伤害加深
	ZhuanJinJianMian,				//专精（鬼道）伤害减免
	ZhuanJinJiaShen,				//专精（鬼道）伤害加深
	
	//从元素系方向区分的
	JianShuShangHaiJiaShen,			//剑术伤害加深（剑术加深‱）
	FaShuShangHaiJiaShen,			//法术伤害加深（法术加深‱）
	LeiXiShangHaiJiaShen,			//雷系元素伤害加深（雷系加深‱）
	HuoXiShangHaiJiaShen,			//火系元素伤害加深（火系加深‱）
	BingXiShangHaiJiaShen,			//冰系元素伤害加深（冰系加深‱）
	FengXiShangHaiJiaShen,			//风系元素伤害加深（风系加深‱）
	DuXiShangHaiJiaShen,			//毒系元素伤害加深（毒系加深‱）
	JianShuShangHaiJianMian,		//剑术伤害减免（剑术减免‱）
	FaShuShangHaiJianMian,			//法术伤害减免（法术减免‱）
	LeiXiShangHaiJianMian,			//雷系元素伤害减免（雷系减免‱）
	HuoXiShangHaiJianMian,			//火系元素伤害减免（火系减免‱）
	BingXiShangHaiJianMian,			//冰系元素伤害减免（冰系减免‱）
	FengXiShangHaiJianMian,			//风系元素伤害减免（风系减免‱）
	DuXiShangHaiJianMian,			//毒系元素伤害减免（毒系减免‱）
	
	KongChanDengJi,					//空蝉等级
	ShanHuaDengJi,					//闪花等级
	ShuangErXiangDengJi,			//双儿响转等级
	ShenZhiBuFaDengJi,				//神之步伐等级
	ShanBiDengJi,					//闪避等级
	MianShangDengJi,				//免伤
	DuochengDaJiDengJi,				//多重打击等级
	PoJiaDengJi,					//破甲等级
	XiXue,							//吸血‱
	KuangZhanShiZhiXue,             //狂战士之血（HP越少伤害越高（损失HP/HP上限 * 威胁比例））
	WeiYa,                          //威压（伤害随回合数增加，每回合增加伤害百分比）
	
	//状态相关
	Faint,							//晕
	Freeze,							//定身
	Chaos,							//混乱
	Rebel,							//反叛
	ForbidenNormalAttack,			//禁止普攻
	ForbidenSpellAttack,			//禁止大招
	ForbidenZhuanJingAttack,		//禁止鬼道
	ImmuteNormalAttack,				//免疫普攻
	ImmuteSpellAttack,				//免疫大招
	ImmuteZhuanJingAttack,			//免疫鬼道
	;

	private static Map<String, HeroBattlePropId> reflect = PropType
			.constructReflect(HeroBattlePropId.class, PropType.HERO_BATTLE);

	/**
	 * 根据id数字获取对相应的HeroBattlePropId
	 * 
	 * @param idName
	 * @return
	 */
	public static HeroBattlePropId get(int idName) {
		return reflect.get(idName);
	}
	
	/**
	 * 判断指定的索引是否有被定义
	 * @param idIndex
	 * @return
	 */
	public static boolean containsIndex(int idIndex) {
		return reflect.containsKey(idIndex);
	}

	private final float minValue;
	private final float maxValue;

	/** 默认最小值是0，最大值是 Float.MAX_VALUE */
	private HeroBattlePropId() {
		this.minValue = 0;
		this.maxValue = Float.MAX_VALUE;
	}

	private HeroBattlePropId(float minValue, float maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * 修正属性值的上下限
	 * 
	 * @param value
	 * @return
	 */
	public float amendValue(float value) {
		if (value < this.minValue) {
			return this.minValue;
		}

		if (value > this.maxValue) {
			return this.maxValue;
		}

		return value;
	}

	@Override
	public PropType getPropType() {
		return PropType.HERO_BATTLE;
	}
}
