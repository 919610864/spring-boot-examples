package com.neo.entity;

import java.util.List;

public class Teacher {

    private Long id;

    private String name;

    private List<Student> studentList;

    public Teacher(Long id, String name, List<Student> studentList) {
        this.id = id;
        this.name = name;
        this.studentList = studentList;
    }
}
