package com.mokylin.bleach.robot.item.view;

import com.mokylin.bleach.common.item.ItemType;

/**
 * 背包类型
 * @author baoliang.shen
 *
 */
public enum InventoryType {
	/**全部*/
	All {
		@Override
		public String getTitle() {return "全部";	}

		@Override
		public String getTip() {return "全部道具";}

		@Override
		public boolean contains(ItemType itemType) {
			return true;
		}
	},
	/**装备*/
	Equip {
		@Override
		public String getTitle() {return "装备";	}

		@Override
		public String getTip() {return "装备";}

		@Override
		public boolean contains(ItemType itemType) {
			if (itemType==ItemType.Equipment) {
				return true;
			}
			return false;
		}
	},
	/**碎片*/
	Fragment {
		@Override
		public String getTitle() {return "碎片";	}

		@Override
		public String getTip() {return "装备碎片和卷轴碎片";}

		@Override
		public boolean contains(ItemType itemType) {
			if (itemType==ItemType.EquipmentMaterialPieces || itemType==ItemType.EquipmentPieces) {
				return true;
			}
			return false;
		}
	},
	/**材料*/
	Material {
		@Override
		public String getTitle() {return "材料";	}

		@Override
		public String getTip() {return "材料";}

		@Override
		public boolean contains(ItemType itemType) {
			if (itemType==ItemType.EquipmentMaterial) {
				return true;
			}
			return false;
		}
	},
	/**消耗*/
	Consumables {
		@Override
		public String getTitle() {return "消耗";	}

		@Override
		public String getTip() {return "消耗品";	}

		@Override
		public boolean contains(ItemType itemType) {
			if (itemType==ItemType.ActiveConsumables || itemType==ItemType.PassiveConsumables) {
				return true;
			}
			return false;
		}
	},
	;

	public abstract String getTitle();

	public abstract String getTip();

	public abstract boolean contains(ItemType itemType);
}
