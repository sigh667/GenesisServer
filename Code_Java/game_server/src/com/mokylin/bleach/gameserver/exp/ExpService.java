package com.mokylin.bleach.gameserver.exp;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.mokylin.bleach.common.exp.ExpData;
import com.mokylin.bleach.common.human.template.ExpTemplate;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.util.MathUtils;

/**
 * 经验等级处理类（Human & Hero）
 * 
 * @author xiao.chang
 */
public class ExpService{
	
	
	private static Map<Integer, ExpTemplate> expMap;
	
	/**
	 * Init Exp Cache Map
	 * 
	 * @param templateservice
	 */
	public void init(TemplateService templateService){
		expMap = templateService.getAll(ExpTemplate.class);
		
		//validate
		for (int i = getCachedSize(); i > 0; i--) {
			if (!expMap.containsKey(i)) {
				String error = String.format("经验等级配置不连续, 缺少Level: %d (Level必须从1级开始连续递增)", i);
				throw new RuntimeException(error);
			}
		}
	}
	
	/**
	 * Get已定义的经验等级数量
	 * 
	 * 
	 * @return 等级数量
	 * @throws IllegalArgumentException 未初始化
	 */
	public static int getCachedSize(){
		checkNotNull(expMap, "ExpService未执行初始化");
		return expMap.size();
	}
	
	/**
	 * Get等级对应的经验上限
	 * 
	 * @param level 等级
	 * 
	 * @return 该等级的经验上限
	 * @throws IllegalArgumentException ExpService未初始化， 或参数不合法
	 */
	public static long getExpByLevel(long level , ExpTemplate.ExpEnum expType){
		checkNotNull(expMap, "expMap为空, ExpService未执行初始化");
		checkArgument(level > 0 || level <= getCachedSize() , 
				String.format("level=%d (level必须>0, 且<=已定义的最高等级 %d)",
						level, expMap.size()));
		return expMap.get((int)level).getExp(expType);
	}
	
	/**
	 * 判断是否可以增加经验
	 * 
	 * @param currLevel  当前等级
	 * @param topLevel  等级上限
	 * @param currExp  当前经验值
	 * @param expType 经验分类 [ExpTemplate.ExpEnum.HUMAN, ExpTemplate.ExpEnum.Hero]
	 * @return boolean
	 */
	public boolean isAddable(long currLevel, long topLevel, long currExp,
			ExpTemplate.ExpEnum expType) {
		if (currLevel > Integer.MAX_VALUE || topLevel > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					String.format("currLevel=%d (currLevel必须>0, 且<=topLevel), topLevel=%d (topLevel必须>0, 且<=Integer.MAX_VALUE)",
							currLevel, topLevel));
		}
		return isAddable((int) currLevel, (int) topLevel, currExp, expType);
	}
	
	/**
	 * 增加经验（Human & Hero），达到最大等级的最大经验则经验不在增加
	 * 建议前置调用isAddable()方法判断是否可以增加经验
	 * 
	 * @param currLevel  当前等级
	 * @param topLevel  等级上限
	 * @param currExp  当前经验值
	 * @param addExp  增加的经验值
	 * @param expType 经验分类 [ExpTemplate.ExpEnum.HUMAN, ExpTemplate.ExpEnum.Hero]
	 * @return ExpData  level和exp
	 */
	public ExpData addExp(long currLevel, long topLevel, long currExp, long addExp, 
			ExpTemplate.ExpEnum expType) {
		if (currLevel > Integer.MAX_VALUE || topLevel > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					String.format("currLevel=%d (currLevel必须>0, 且<=topLevel), topLevel=%d (topLevel必须>0, 且<=Integer.MAX_VALUE)",
							currLevel, topLevel));
		}
		return addExp((int)currLevel, (int)topLevel, currExp, addExp, expType);
	}
	
	/**
	 * 增加经验（Human & Hero），达到最大等级的最大经验则经验不在增加
	 * 建议前置调用isAddable()方法判断是否可以增加经验
	 * 
	 * @param currLevel  当前等级
	 * @param topLevel  等级上限
	 * @param currExp  当前经验值
	 * @param addExp  增加的经验值
	 * @param expType 经验分类 [ExpTemplate.ExpEnum.HUMAN, ExpTemplate.ExpEnum.Hero]
	 * @return ExpData  level和exp
	 */
	private ExpData addExp(int currLevel, int topLevel, long currExp, long addExp, ExpTemplate.ExpEnum expType) {

		if (expMap == null || expType == null || currExp < 0 || addExp < 0
				|| MathUtils.longAddOverflow(currExp, addExp)
				|| currLevel > Integer.MAX_VALUE || topLevel > Integer.MAX_VALUE
				|| currLevel <= 0 || currLevel > topLevel || currLevel > getCachedSize()) {
			throw new IllegalArgumentException(
					String.format("参数有误：currLevel=%d (currLevel必须>0, 且<=topLevel), topLevel=%d (topLevel必须>0, 且<=Integer.MAX_VALUE), currExp=%d (currExp必须>=0), addExp=%d (addExp必须>=0), expType=%s (expType不能为null)",
							currLevel, topLevel, currExp, addExp, expType));
		}
		
		int tmpTop = topLevel < getCachedSize() ? topLevel : getCachedSize();
		
		long upgradeExp = getExpByLevel(currLevel, expType);
		currExp += addExp;
		
		while (currExp >= upgradeExp) {
			if (++currLevel > tmpTop) {// 超过最高等级
				currLevel = tmpTop;
				currExp = upgradeExp;
				break;
			}
			currExp -= upgradeExp;
			upgradeExp = getExpByLevel(currLevel, expType);
		}
		
		return new ExpData(currLevel,currExp);
	}
	
	/**
	 * 判断是否可以增加经验
	 * 
	 * @param currLevel  当前等级
	 * @param topLevel  等级上限
	 * @param currExp  当前经验值
	 * @param expType 经验分类 [ExpTemplate.ExpEnum.HUMAN, ExpTemplate.ExpEnum.Hero]
	 * @return boolean
	 */
	private boolean isAddable(int currLevel, int topLevel, long currExp,
			ExpTemplate.ExpEnum expType) {
		if (currExp < 0 || currLevel <= 0 || currLevel > topLevel
				|| currLevel > Integer.MAX_VALUE || topLevel > Integer.MAX_VALUE
				|| expType == null || expMap == null|| currLevel > getCachedSize()) {
			throw new IllegalArgumentException(
					String.format("currLevel=%d (currLevel必须>0, 且<=topLevel), topLevel=%d (topLevel必须>0, 且<=Integer.MAX_VALUE), currExp=%d (currExp必须>=0), expType=%s (expType不能为null)",
							currLevel, topLevel, currExp, expType));
		}
		
		int tmpTop = topLevel < getCachedSize() ? topLevel : getCachedSize();
		
		if (currLevel == tmpTop)
			return getExpByLevel(tmpTop,expType) - currExp > 0;
		else
			return true;
	}
	
}
