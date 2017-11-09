package com.mokylin.bleach.gameserver.chat.cmd;

import java.util.List;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.ItemMessage.GCItemUpdate;
import com.mokylin.bleach.protobuf.ItemMessage.ItemData;

/**
 * 给物品的GM命令。<p>
 * 
 * 格式：giveitem itemTemplateId amount[正整数]
 * 
 * @author pangchong
 *
 */
public class GiveItemCmdFunc implements IGmCmdFunction {

	@Override
	public String getGmCmd() {
		return "giveitem";
	}

	@Override
	public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
		if(paramList == null || paramList.isEmpty() || paramList.size() < 2) return;
		
		int itemTemplateId = Integer.valueOf(paramList.get(0));
		if(!GlobalData.getTemplateService().isTemplateExist(itemTemplateId, ItemTemplate.class)) return;
		
		int itemAmount = Integer.valueOf(paramList.get(1));
		if(itemAmount <= 0) return;
		
		if(human.getInventory().addItem(itemTemplateId, itemAmount)){
			human.sendMessage(GCItemUpdate.newBuilder().addUpdatedItems(ItemData.newBuilder().setTemplateId(itemTemplateId).setOverlap(itemAmount)));
		}
	}

}
