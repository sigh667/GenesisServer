package com.mokylin.bleach.gamedb.orm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.descriptor.sql.IntegerTypeDescriptor;

import com.mokylin.bleach.core.orm.hibernate.type.UserDefinedType;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.orm.vo.HeroEquip;

/**
 * 自定义Hibernate类型，用于{@link HeroEntity}中标记HeroEquip类型。
 * 
 * @author pangchong
 *
 */
public class HeroEquipType extends UserDefinedType<HeroEquip[]> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 该类型映射到数据库中的列名字，总共18列。
	 */
	private static final String[] registrationKeys = new String[]{"equipTemplateId1", "equipEnchantLevel1", "equipEnchantExp1", "equipTemplateId2", "equipEnchantLevel2", "equipEnchantExp2",
		"equipTemplateId3", "equipEnchantLevel3", "equipEnchantExp3", "equipTemplateId4", "equipEnchantLevel4", "equipEnchantExp4", 
		"equipTemplateId5", "equipEnchantLevel5", "equipEnchantExp5", "equipTemplateId6", "equipEnchantLevel6", "equipEnchantExp6"};
	
	public HeroEquipType(){
		super(18, IntegerTypeDescriptor.INSTANCE, HeroEquip[].class, registrationKeys);
	}
	
	@Override
	public boolean isEqual(Object one, Object another) {
		return one == another
				|| (one != null && another != null && Arrays.equals((HeroEquip[])one, (HeroEquip[])another));
	}

	/**
	 * 该方法总共从t_hero表中读取18列，组装6个HeroEquip对象成一个数组。<p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		assert names.length == this.getColumnNum();
		
		int equipIndex = 0;
		HeroEquip[] equips = new HeroEquip[6];
		int columnIndex = 1; //表示读取的列，从1-3循环，每3列一组，分别是equipTemplateId， equipEnchantLevel和equipEnchantExp
		for(int i=0; i<names.length; i++){
			Object o = rs.getObject(names[i]);
			if(columnIndex == 1){
				if(o == null){
					equips[equipIndex] = null;
				}else{
					equips[equipIndex] = new HeroEquip();
					equips[equipIndex].setTemplateId((int)o);
				}
			}else if(columnIndex == 2){
				if(equips[equipIndex] != null){
					equips[equipIndex].setEnchantLevel((int)o);
				}
			}else{
				if(equips[equipIndex] != null){
					equips[equipIndex].setEnchantExp((int)o);
				}
				equipIndex += 1;
				columnIndex = 0;
			}
			columnIndex += 1;
		}
		return equips;
	}

	/**
	 * 将HeroEquip数组的值设置到PreparedStatement中，总共18个值；如果数组为null，则全部设置为null；如果数组
	 * 中某个对象为null，则对应的列设置为null。<p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if(value == null){
			for(int columnIndex=0; columnIndex<this.getColumnNum(); columnIndex++){
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
			}
			return;
		}
		HeroEquip[] equips = (HeroEquip[]) value;
		int columnIndex = 0;
		int arrayIndex = 0;
		while(columnIndex < this.getColumnNum()){
			if(equips.length > arrayIndex && equips[arrayIndex] != null){
				IntegerType.INSTANCE.set(st, equips[arrayIndex].getTemplateId(), index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, equips[arrayIndex].getEnchantLevel(), index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, equips[arrayIndex].getEnchantExp(), index, session);
				index += 1;
			}else{
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
			}
			arrayIndex += 1;
			columnIndex += 3;
		}
	}

	@Override
	public Object deepCopy(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if(value == null) return null;
		HeroEquip[] heroEquips = (HeroEquip[]) value;
		if(heroEquips.length == 0) return new HeroEquip[0];
		
		HeroEquip[] newEquips = new HeroEquip[heroEquips.length];
		for(int i = 0; i<newEquips.length; i++){
			if(heroEquips[i]!=null){
				newEquips[i] = new HeroEquip(heroEquips[i].getTemplateId(), heroEquips[i].getEnchantLevel(), heroEquips[i].getEnchantExp());
			}			
		}
		return newEquips;
	}
}
