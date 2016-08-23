package com.mbv.persist.dao;

import com.mbv.persist.entity.BaseEntity;

import java.util.Collection;
import java.util.Map;

public interface BaseEntityDAO<I,T extends BaseEntity<I>> {

    public abstract T get(I id);

    public abstract T getAndEvict(I id);

    public abstract Map<I,T> get(Collection<I> id);

    public abstract void create(T obj);

    public abstract void update(T obj);

    public abstract void updateAndFlush(T obj);

    public abstract void bulkCreate(Collection<T> objList);

    public abstract void bulkUpdate(Collection<T> objList);

    public abstract void delete(I id);

    public void mergeAndFlush(T obj);

}
