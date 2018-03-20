package com.mokylin.bleach.gameserver.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import com.genesis.common.core.GlobalData;
import com.genesis.common.item.template.ItemCompoundMaterial;
import com.genesis.common.item.template.ItemCompoundTemplate;

import java.util.Map;

public class ItemCheckUtil {

    private static ImmutableSet<Integer> allMaterials = null;

    public static boolean isMaterial(int itemTemplateId) {
        if (allMaterials == null) {
            init();
        }
        return allMaterials.contains(itemTemplateId);
    }

    private static void init() {
        Map<Integer, ItemCompoundTemplate> allCompoundTemplates =
                GlobalData.getTemplateService().getAll(ItemCompoundTemplate.class);
        Builder<Integer> builder = ImmutableSet.builder();
        for (ItemCompoundTemplate each : allCompoundTemplates.values()) {
            for (ItemCompoundMaterial eachMaterial : each.getCompoundMaterials()) {
                if (eachMaterial == null) {
                    continue;
                }
                builder.add(eachMaterial.getMaterialTemplateId());
            }
        }
        allMaterials = builder.build();
    }
}
