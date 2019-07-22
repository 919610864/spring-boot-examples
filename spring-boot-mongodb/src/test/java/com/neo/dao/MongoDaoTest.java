package com.neo.dao;


import com.neo.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDaoTest {

    @Autowired
    private MongoDao<UserEntity> mongoDao;

    @Test
    public void testSave(){
        UserEntity user = null;
        Long beginTime = System.currentTimeMillis();
        for(int i=100000;i<5000000;i++){
            user=new UserEntity();
            user.setId(new Long(i));
            user.setUserName("小明"+i);
            user.setPassWord("密码"+i);
            mongoDao.save(user);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("执行完毕,time:"+(endTime-beginTime));
    }

    @Test
    public void findAll(){
        List<UserEntity> list = mongoDao.findAll(UserEntity.class);
        System.out.println("list:"+list.size());
    }


}
