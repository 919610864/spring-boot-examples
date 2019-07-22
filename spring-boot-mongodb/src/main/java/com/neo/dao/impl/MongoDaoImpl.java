package com.neo.dao.impl;

import com.neo.dao.MongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDaoImpl<T> implements MongoDao<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public List<T> findAll(Class<T> userEntityClass) {
        return mongoTemplate.findAll(userEntityClass);
    }
}
