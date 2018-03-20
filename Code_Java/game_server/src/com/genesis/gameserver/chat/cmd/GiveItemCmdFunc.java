package com.genesis.gameserver.chat.cmd;

import com.genesis.common.core.GlobalData;
import com.genesis.common.item.template.ItemTemplate;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.genesis.protobuf.ItemMessage.GCItemUpdate;
import com.genesis.protobuf.db.DBInventoryBlob.ItemData;

import java.util.List;

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
        if (paramList == null || paramList.isEmpty() || paramList.size() < 2) {
            return;
        }

        int itemTemplateId = Integer.valueOf(paramList.get(0));
        if (!GlobalData.getTemplateService().isTemplateExist(itemTemplateId, ItemTemplate.class)) {
            return;
        }

        int itemAmount = Integer.valueOf(paramList.get(1));
        if (itemAmount <= 0) {
            return;
        }

        if (human.getInventory().addItem(itemTemplateId, itemAmount)) {
            human.sendMessage(GCItemUpdate.newBuilder().addUpdatedItems(
                    ItemData.newBuilder().setTemplateId(itemTemplateId).setOverlap(itemAmount)));
        }
    }

}
