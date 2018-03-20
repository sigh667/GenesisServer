package com.genesis.core.orm.hibernate;

import com.genesis.core.orm.BaseEntity;

import java.io.Serializable;

public class EntityToDelete {

    public Class<? extends BaseEntity> entityClass;
    public Serializable id;
}
