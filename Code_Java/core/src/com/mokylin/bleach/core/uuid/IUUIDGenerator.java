package com.mokylin.bleach.core.uuid;

import com.mokylin.bleach.core.uuid.type.IUUIDType;

/**
 * UUID生成器
 * 
 * <p>此接口与{@link IUUIDType}结合使用，只要在com.mokylin.bleach.core.uuid.type包中添加
 * 一个新的{@link IUUIDType}的实现，这里就可以支持新类型的UUID生成
 * 
 * @author yaguang.xiao
 *
 */

public interface IUUIDGenerator {
	
	/**
	 * 获取指定类型的下一个有效UUID
	 * 
	 * <p>若指定类型不存在，则返回-1
	 * @param uuidType
	 * @return
	 */
	long getNextUUID(IUUIDType uuidType);
}
