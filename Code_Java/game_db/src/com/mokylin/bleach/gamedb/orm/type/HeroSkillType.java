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
import com.mokylin.bleach.gamedb.orm.vo.HeroSkill;

/**
 * 自定义Hibernate类型，用于{@link HeroEntity}中标记HeroSkill类型。
 * 
 * @author pangchong
 *
 */
public class HeroSkillType extends UserDefinedType<HeroSkill[]> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 该类型映射到数据库中的列名字，总共12列。
	 */
	private static final String[] registrationKeys = new String[]{"skillTemplateId1", "skillLevel1", "skillTemplateId2", "skillLevel2", "skillTemplateId3", "skillLevel3", "skillTemplateId4", "skillLevel4", "skillTemplateId5", "skillLevel5", "skillTemplateId6", "skillLevel6"};
	
	public HeroSkillType(){
		super(12, IntegerTypeDescriptor.INSTANCE, HeroSkill[].class, registrationKeys);
	}
	
	@Override
	public boolean isEqual(Object one, Object another) {
		return one == another
				|| (one != null && another != null && Arrays.equals((HeroSkill[])one, (HeroSkill[])another));
	}

	/**
	 * 该方法总共从t_hero表中读取12列，组装6个HeroSkill对象成一个数组。<p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		assert names.length == this.getColumnNum();
		
		int skillIndex = 0;
		HeroSkill[] skills = new HeroSkill[6];
		int columnIndex = 1; //表示读取的列，从1-2循环，每2列一组，分别是skillTemplateId和skillLevel
		for(int i=0; i<names.length; i++){
			Object o = rs.getObject(names[i]);
			if(columnIndex == 1){
				if(o == null){
					skills[skillIndex] = null;
				}else{
					skills[skillIndex] = new HeroSkill();
					skills[skillIndex].setTemplateId((int)o);
				}
			}else{
				if(skills[skillIndex] != null){
					skills[skillIndex].setLevel((int)o);
				}
				skillIndex += 1;
				columnIndex = 0;
			}
			columnIndex += 1;
		}
		return skills;
	}

	/**
	 * 将HeroSkill数组的值设置到PreparedStatement中，总共12个值；如果数组为null，则全部设置为null；如果数组
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
		HeroSkill[] skills = (HeroSkill[]) value;
		int columnIndex = 0;
		int arrayIndex = 0;
		while(columnIndex < this.getColumnNum()){
			if(skills.length > arrayIndex && skills[arrayIndex] != null){
				IntegerType.INSTANCE.set(st, skills[arrayIndex].getTemplateId(), index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, skills[arrayIndex].getLevel(), index, session);
				index += 1;
			}else{
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
				IntegerType.INSTANCE.set(st, null, index, session);
				index += 1;
			}
			arrayIndex += 1;
			columnIndex += 2;
		}
	}

	@Override
	public Object deepCopy(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if(value == null) return null;
		HeroSkill[] skills = (HeroSkill[]) value;
		if(skills.length == 0) return new HeroSkill[0];
		
		HeroSkill[] newSkills = new HeroSkill[skills.length];
		for(int i = 0; i<newSkills.length; i++){
			if(skills[i]!=null){
				newSkills[i] = new HeroSkill(skills[i].getTemplateId(), skills[i].getLevel());
			}			
		}
		return newSkills;
	}
}
