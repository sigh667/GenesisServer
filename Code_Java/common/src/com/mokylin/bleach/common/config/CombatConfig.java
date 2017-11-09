package com.mokylin.bleach.common.config;

import scala.collection.mutable.StringBuilder;

/**
 * 战斗系统全局配置
 * @author baoliang.shen
 *
 */
public class CombatConfig {

	public static final String fileName = "combat.xml";
	//闪避
	private boolean ShanBiSwitch = false;
	private float ShanBiQuanJuXiShu = 0.1f;
	//格挡
	private float GeDangGaiLvQuanJuXiShu = 0.1f;
	private float GeDangQuanJuXiShu = 0.5f;
	//多重打击
	private float DuoChongDaJiQuanJuChuFaXiShu = 0.0f;
	private float DuoChongDaJiaChuShiBeiLv = 0.1f;
	private float DuoChongDaJiQuanJuXiuZhengXiShu = 0.1f;
	//破甲
	private float PoJiaXiShu = 1.0f;
	private float PoJiaGaiLvQuanJuXiShu = 0.1f;
	//暴击
	private float BaoJiQuanJuXiuZhengXiShu = 1.0f;
	//濒死
	private float BinSiPanDingXiShu = 0.2f;
	private float BinSiShangHaiXiShu = 2.0f;
	//元素
	private float YuanSuXiuZhengXiShuJianShu = 1.1f;
	private float YuanSuXiuZhengXiShuFaShu = 1.2f;
	private float YuanSuXiuZhengXiShuLeiXi = 1.2f;
	private float YuanSuXiuZhengXiShuHuoXi = 1.2f;
	private float YuanSuXiuZhengXiShuBingXi = 1.2f;
	private float YuanSuXiuZhengXiShuFengXi = 1.2f;
	private float YuanSuXiuZhengXiShuDuXi = 1.2f;

	//矩形宽度（是半宽不是全宽）
	private float JuXingKuanDu = 1f;
	//战斗左边整体站位与Z轴的夹角，以6点钟方向为0度，顺时针为负，逆时针为正，单位度
	private float LeftPositionAngle = 0f;
	//战斗右边整体站位与Z轴的夹角，以6点钟方向为0度，顺时针为负，逆时针为正，单位度
	private float RightPositionAngle = 0f;

	//怒气刀技攻击方回复比例
	private float NuQiDaoJiGongJiFangHuiFuBiLi = 0.0f;
	//怒气刀技被击方回复比例
	private float NuQiDaoJiBeiJiFangHuiFuBiLi = 2.0f;
	//怒气普攻被击方回复比例
	private float NuQiPuGongBeiJiFangHuiFuBiLi = 1.0f;
	//怒气闪避攻击方回复比例
	private float NuQiShanBiGongJiFangHuiFuBiLi = 0.5f;
	//怒气闪避被击方回复比例
	private float NuQiShanBiBeiJiFangHuiFuBiLi = 0.2f;

	//怒气团队初始值
	private int NuQiMaxTeamChuShi = 600;

	//跑回时间
	private float RunBackTime = 0.2f;

	//子弹初速度
	private float BulletSpeed = 5.0f;

	//子弹加速度
	private float BulletAccSpeed = 34.8f;

	//位移差
	private float InOriginalPositionThreshold = 4.0f;

	//每行所站人数
	private int FighterNumberPerRow = 3;

	//释放技能时检查法力值开关
	private boolean IsCheckHasEnoughMana = true;

	//释放技能时检查CD开关
	private boolean IsCheckSpellCD = true;

	//重击判定比例
	private float CriticalHitHPFlag = 0.1f;

	//是否检查连携概率
	private boolean IsCheckComboRate = true;
	//全局连携开关
	private boolean EnableComboSkill = true;


	public boolean isShanBiSwitch() {
		return ShanBiSwitch;
	}
	public float getShanBiQuanJuXiShu() {
		return ShanBiQuanJuXiShu;
	}
	public float getGeDangGaiLvQuanJuXiShu() {
		return GeDangGaiLvQuanJuXiShu;
	}
	public float getGeDangQuanJuXiShu() {
		return GeDangQuanJuXiShu;
	}
	public float getDuoChongDaJiQuanJuChuFaXiShu() {
		return DuoChongDaJiQuanJuChuFaXiShu;
	}
	public float getDuoChongDaJiaChuShiBeiLv() {
		return DuoChongDaJiaChuShiBeiLv;
	}
	public float getDuoChongDaJiQuanJuXiuZhengXiShu() {
		return DuoChongDaJiQuanJuXiuZhengXiShu;
	}
	public float getPoJiaXiShu() {
		return PoJiaXiShu;
	}
	public float getPoJiaGaiLvQuanJuXiShu() {
		return PoJiaGaiLvQuanJuXiShu;
	}
	public float getBaoJiQuanJuXiuZhengXiShu() {
		return BaoJiQuanJuXiuZhengXiShu;
	}
	public float getBinSiPanDingXiShu() {
		return BinSiPanDingXiShu;
	}
	public float getBinSiShangHaiXiShu() {
		return BinSiShangHaiXiShu;
	}
	public float getYuanSuXiuZhengXiShuJianShu() {
		return YuanSuXiuZhengXiShuJianShu;
	}
	public float getYuanSuXiuZhengXiShuFaShu() {
		return YuanSuXiuZhengXiShuFaShu;
	}
	public float getYuanSuXiuZhengXiShuLeiXi() {
		return YuanSuXiuZhengXiShuLeiXi;
	}
	public float getYuanSuXiuZhengXiShuHuoXi() {
		return YuanSuXiuZhengXiShuHuoXi;
	}
	public float getYuanSuXiuZhengXiShuBingXi() {
		return YuanSuXiuZhengXiShuBingXi;
	}
	public float getYuanSuXiuZhengXiShuFengXi() {
		return YuanSuXiuZhengXiShuFengXi;
	}
	public float getYuanSuXiuZhengXiShuDuXi() {
		return YuanSuXiuZhengXiShuDuXi;
	}
	public float getJuXingKuanDu() {
		return JuXingKuanDu;
	}
	public float getLeftPositionAngle() {
		return LeftPositionAngle;
	}
	public float getRightPositionAngle() {
		return RightPositionAngle;
	}
	public float getNuQiDaoJiGongJiFangHuiFuBiLi() {
		return NuQiDaoJiGongJiFangHuiFuBiLi;
	}
	public float getNuQiDaoJiBeiJiFangHuiFuBiLi() {
		return NuQiDaoJiBeiJiFangHuiFuBiLi;
	}
	public float getNuQiPuGongBeiJiFangHuiFuBiLi() {
		return NuQiPuGongBeiJiFangHuiFuBiLi;
	}
	public float getNuQiShanBiGongJiFangHuiFuBiLi() {
		return NuQiShanBiGongJiFangHuiFuBiLi;
	}
	public float getNuQiShanBiBeiJiFangHuiFuBiLi() {
		return NuQiShanBiBeiJiFangHuiFuBiLi;
	}
	public int getNuQiMaxTeamChuShi() {
		return NuQiMaxTeamChuShi;
	}
	public float getRunBackTime() {
		return RunBackTime;
	}
	public float getBulletSpeed() {
		return BulletSpeed;
	}
	public float getBulletAccSpeed() {
		return BulletAccSpeed;
	}
	public float getInOriginalPositionThreshold() {
		return InOriginalPositionThreshold;
	}
	public int getFighterNumberPerRow() {
		return FighterNumberPerRow;
	}
	public boolean isIsCheckHasEnoughMana() {
		return IsCheckHasEnoughMana;
	}
	public boolean isIsCheckSpellCD() {
		return IsCheckSpellCD;
	}
	public float getCriticalHitHPFlag() {
		return CriticalHitHPFlag;
	}
	public boolean isIsCheckComboRate() {
		return IsCheckComboRate;
	}
	public boolean isEnableComboSkill() {
		return EnableComboSkill;
	}

	/**
	 * 校验数据合法性
	 */
	public void validate() {
		StringBuilder errMsg = new StringBuilder();
		final String lineBreak = System.getProperty("line.separator");

		//具体检测代码
		if (NuQiMaxTeamChuShi < 0) {
			errMsg.append("怒气团队初始值（NuQiMaxTeamChuShi）不能<0").append(lineBreak);
		}

		// 检测到错误则抛出异常
		if (!errMsg.isEmpty()) {
			throw new RuntimeException(errMsg.toString());
		}
	}
}
