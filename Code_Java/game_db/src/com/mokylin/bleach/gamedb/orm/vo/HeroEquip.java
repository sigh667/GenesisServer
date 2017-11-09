package com.mokylin.bleach.gamedb.orm.vo;

public class HeroEquip {

	private int templateId;
	private int enchantLevel;
	private int enchantExp;
	
	public HeroEquip() {}
	
	public HeroEquip(int templateId, int enchantLevel, int enchantExp) {
		super();
		this.templateId = templateId;
		this.enchantLevel = enchantLevel;
		this.enchantExp = enchantExp;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public int getEnchantLevel() {
		return enchantLevel;
	}
	public void setEnchantLevel(int enchantLevel) {
		this.enchantLevel = enchantLevel;
	}
	public int getEnchantExp() {
		return enchantExp;
	}
	public void setEnchantExp(int enchantExp) {
		this.enchantExp = enchantExp;
	}
	
}
