package com.neo.dao;

import java.util.List;

public interface MongoDao<T> {

    public void save(T entity);

    List<T> findAll(Class<T> userEntityClass);
}
