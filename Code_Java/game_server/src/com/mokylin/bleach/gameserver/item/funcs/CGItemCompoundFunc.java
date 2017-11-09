package com.mokylin.bleach.gameserver.item.funcs;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.item.template.ItemCompoundMaterial;
import com.mokylin.bleach.common.item.template.ItemCompoundTemplate;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.ItemInfo;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.ItemMessage.CGItemCompound;

/**
 * 处理客户端使用物品消息的函数对象。
 * 
 * 该函数对象在PlayerActor中执行。
 * 
 * @author pangchong
 *
 */
public class CGItemCompoundFunc extends AbstractClientMsgFunc<CGItemCompound, Human, ServerGlobals>{

	@Override
	public void handle(Player player, CGItemCompound msg, Human human, ServerGlobals sGlobals) {
		ItemCompoundTemplate itemCompoundTempalte = GlobalData.getTemplateService().get(msg.getToTemplateId(), ItemCompoundTemplate.class);
		if(itemCompoundTempalte == null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		if(!human.isMoneyEnough(Currency.GOLD, itemCompoundTempalte.getCompoundPrice())){
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		ItemCompoundMaterial[] materials = itemCompoundTempalte.getCompoundMaterials();
		//下面写的有些蛋疼了，先这样吧。
		if(materials.length == 1){
			if(human.getInventory().deleteItem(materials[0].getMaterialTemplateId(), materials[0].getAmount())){
				human.costMoney(Currency.GOLD, itemCompoundTempalte.getCompoundPrice());
				human.getInventory().addItem(msg.getToTemplateId(), 1);
			}else{
				human.notifyDataErrorAndDisconnect();
			}
			return;
		}
		
		ItemInfo[] deleteList = new ItemInfo[materials.length];
		for(int i=0; i<materials.length; i++){
			ItemCompoundMaterial eachMaterial = materials[i];
			if(human.getInventory().getItemAmount(eachMaterial.getAmount()) <= eachMaterial.getAmount()){
				human.notifyDataErrorAndDisconnect();
				return;
			}
			deleteList[i] = ItemInfo.of(eachMaterial.getMaterialTemplateId(), eachMaterial.getAmount());
		}
		
		if(human.getInventory().batchDeleteItems(deleteList)){
			human.costMoney(Currency.GOLD, itemCompoundTempalte.getCompoundPrice());
			human.getInventory().addItem(msg.getToTemplateId(), 1);
		}else{
			human.notifyDataErrorAndDisconnect();
		}
	}
}
