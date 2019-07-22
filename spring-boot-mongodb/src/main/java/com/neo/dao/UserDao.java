package com.neo.dao;

import com.neo.entity.UserEntity;

import java.util.List;

/**
 * Created by summer on 2017/5/5.
 */
public interface UserDao  {

    public void saveUser(UserEntity user);

    public UserEntity findUserByUserName(String userName);

    public int updateUser(UserEntity user);

    public void deleteUserById(Long id);

    public List<UserEntity> findAll(Class<UserEntity> clazz, String col);

    List findAll(Class<UserEntity> userEntityClass);
}
