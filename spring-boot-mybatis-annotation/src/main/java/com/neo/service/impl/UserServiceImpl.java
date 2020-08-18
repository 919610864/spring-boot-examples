package com.neo.service.impl;

import com.neo.entity.UserEntity;
import com.neo.mapper.UserMapper;
import com.neo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    public List<UserEntity> getUsers() {
        List<UserEntity> users=userMapper.getAll();
        return users;
    }

    public UserEntity getUser(Long id) {
        UserEntity user=userMapper.getOne(id);
        return user;
    }

    public void save(UserEntity user) {
        userMapper.insert(user);
    }

    public void update(UserEntity user) {
        userMapper.update(user);
    }

    public void delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
    }


}
