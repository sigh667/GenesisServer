package com.mokylin.bleach.core.orm.hibernate;

import java.io.Serializable;

import com.mokylin.bleach.core.orm.BaseEntity;

public class EntityToDelete {

	public Class<? extends BaseEntity> entityClass;
	public Serializable id;
}
