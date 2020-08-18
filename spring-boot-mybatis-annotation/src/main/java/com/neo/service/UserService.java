package com.neo.service;

import com.alicp.jetcache.anno.Cached;
import com.neo.entity.UserEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    List<UserEntity> getUsers();


    @Cached(name = "userCache-",key = "#userId",expire = 3600)
    UserEntity getUser(Long id);

    void save(UserEntity user);

    void update(UserEntity user);

    void delete(@PathVariable("id") Long id);

}
