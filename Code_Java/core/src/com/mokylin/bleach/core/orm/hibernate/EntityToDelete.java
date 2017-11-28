package com.mokylin.bleach.core.orm.hibernate;

import com.mokylin.bleach.core.orm.BaseEntity;

import java.io.Serializable;

public class EntityToDelete {

    public Class<? extends BaseEntity> entityClass;
    public Serializable id;
}
