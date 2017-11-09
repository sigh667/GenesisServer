package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.common.human.vip.VipPrivilege;
import com.mokylin.bleach.common.human.vip.VipPrivilegeType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class VipTemplate extends VipTemplateVO {
	private VipPrivilege[] vipPrivileges = new VipPrivilege[VipPrivilegeType.values().length];
	
	@Override
	public void patchUp() throws Exception {
		super.patchUp();
		if (this.getPrivileges().length < vipPrivileges.length) return;
		
		for (int i = 0; i < vipPrivileges.length; i++) {
			vipPrivileges[i] = new VipPrivilege(privileges[i]);
		}
	}

	@Override
	public void check() throws TemplateConfigException {
		//模板中配置的特权数量 不能< VIP枚举数量
		if (this.getPrivileges().length < vipPrivileges.length) {
			throwTemplateException("VIP模板配置错误，配置的类型数量小于程序定义的数量");
		}
				
		for (int privilege : this.getPrivileges()) {
			if (privilege == 0) {
				throwTemplateException(String.format("VIP权限配置项不能为【 】或【0】，功能未开启统一用【%d】标识",
						VipPrivilege.DISABLED));
			}
		}
	}
	
	public VipPrivilege getVipPrivileges(VipPrivilegeType type){
		return vipPrivileges[type.ordinal()];
	}
}
