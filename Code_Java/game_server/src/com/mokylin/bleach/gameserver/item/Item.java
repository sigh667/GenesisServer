package com.mokylin.bleach.gameserver.item;

import java.sql.Timestamp;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.IItem;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.mokylin.bleach.gameserver.core.persistance.IDataUpdater;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 道具
 * @author baoliang.shen
 *
 */
public class Item extends ObjectInSqlImpl<Long, ItemEntity> implements IItem{

	/**道具ID*/
	private Long id;
	/**叠加数量*/
	private int overlap;
	/**创建时间*/
	private Timestamp createTime;

	/**所有者*/
	private Human owner;
	/**道具模板 */
	private ItemTemplate template;

	private Item(IDataUpdater dataUpdater) {
		super(dataUpdater);
	}
	
	public static Item buildFromTemplateId(int templateId, int count, Human owner){
		ItemTemplate template = GlobalData.getTemplateService().get(templateId, ItemTemplate.class);
		long uuid = owner.getServerGlobals().getUUIDGenerator().getNextUUID(UUIDType.Item);
		Item item = new Item(owner.getDataUpdater());
		item.owner = owner;
		item.template = template;
		item.setId(uuid);
		item.setOverlap(count);
		return item;
	}

	public static Item buildFromItemEntity(ItemEntity entity, Human owner) {
		final int templateId = entity.getTemplateId();
		ItemTemplate template = GlobalData.getTemplateService().get(templateId,
				ItemTemplate.class);
		Item item = new Item(owner.getDataUpdater());
		item.owner = owner;
		item.template = template;
		item.fromEntity(entity);
		return item;
	}

	@Override
	public Long getDbId() {
		return id;
	}

	@Override
	public ItemEntity toEntity() {
		ItemEntity itemEntity = new ItemEntity();
		itemEntity.setCreateTime(createTime);
		itemEntity.setHumanId(owner.getDbId());
		itemEntity.setId(id);
		itemEntity.setOverlap(overlap);
		itemEntity.setTemplateId(this.getTemplateId());
		return itemEntity;
	}

	@Override
	public void fromEntity(ItemEntity entity) {
		this.createTime = entity.getCreateTime();
		this.id = entity.getId();
		this.overlap = entity.getOverlap();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOverlap() {
		return overlap;
	}

	public void setOverlap(int overlap) {
		this.overlap = overlap;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * 道具模板ID
	 * 
	 * @return 如果此道具已经绑定了模板，返回模板id，否则返回-1
	 */
	public int getTemplateId() {
		return template != null ? template.getId() : -1;
	}

	public Human getOwner() {
		return owner;
	}

	public ItemTemplate getTemplate() {
		return this.template;
	}
}
