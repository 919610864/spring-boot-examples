package com.neo.dao;

import com.neo.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by summer on 2017/5/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testFindAll(){
        List list = userDao.findAll(UserEntity.class);
    }

    @Test
    public void testSaveUser() throws Exception {
        UserEntity user = null;
        Long beginTime = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            user=new UserEntity();
            user.setId(new Long(i));
            user.setUserName("小明"+i);
            user.setPassWord("密码"+i);
            userDao.saveUser(user);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("执行完毕,time:"+(endTime-beginTime));

    }

    @Test
    public void findUserByUserName(){
       UserEntity user= userDao.findUserByUserName("小明");
       System.out.println("user is "+user);
    }

    @Test
    public void updateUser(){
        UserEntity user=new UserEntity();
        user.setId(2l);
        user.setUserName("天空");
        user.setPassWord("fffxxxx");
        userDao.updateUser(user);
    }

    @Test
    public void deleteUserById(){
        userDao.deleteUserById(1l);
    }

}
