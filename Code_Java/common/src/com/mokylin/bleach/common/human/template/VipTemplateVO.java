package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * VIP模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class VipTemplateVO extends TemplateObject {
	
	/** 达到该经验值则升级 */
	@ExcelCellBinding(offset = 1)
	protected long upgradeExp;

	/** 对应VIP的权限 */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23")
	protected int[] privileges;


	public long getUpgradeExp() {
		return this.upgradeExp;
	}

	public void setUpgradeExp(long upgradeExp) {
		if (upgradeExp == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[达到该经验值则升级]upgradeExp不可以为0");
		}
		if (upgradeExp < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[达到该经验值则升级]upgradeExp的值不得小于0");
		}
		this.upgradeExp = upgradeExp;
	}
	
	public int[] getPrivileges() {
		return this.privileges;
	}

	public void setPrivileges(int[] privileges) {
		if (privileges == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[对应VIP的权限]privileges不可以为空");
		}	
		this.privileges = privileges;
	}
	

	@Override
	public String toString() {
		return "VipTemplateVO[upgradeExp=" + upgradeExp + ",privileges=" + privileges + ",]";

	}
}