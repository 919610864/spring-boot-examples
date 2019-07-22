package com.neo.dao;


import com.neo.entity.Student;
import com.neo.entity.Teacher;
import com.neo.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherTest {

    @Autowired
    private MongoDao<Teacher> mongoDao;

    @Test
    public void save(){
        List<Student> studentList = new ArrayList<>();
        Student s1 = new Student(1l,"张三1",10,"aa",new Date());
        Student s2 = new Student(1l,"张三2",10,"aa",new Date());
        Student s3 = new Student(1l,"张三3",10,"aa",new Date());
        Student s4 = new Student(1l,"张三4",10,"aa",new Date());
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);
        studentList.add(s4);
        Teacher teacher = new Teacher(1l,"张老师",studentList);
        mongoDao.save(teacher);
    }
}
