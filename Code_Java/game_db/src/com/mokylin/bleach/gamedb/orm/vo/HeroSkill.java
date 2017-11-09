package com.mokylin.bleach.gamedb.orm.vo;

public class HeroSkill {
	
	private int templateId;
	
	private int level;
	
	public HeroSkill(){}

	public HeroSkill(int templateId, int level) {
		this.templateId = templateId;
		this.level = level;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
