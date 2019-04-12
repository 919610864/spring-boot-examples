package com.neo.entity;

import java.util.Date;

public class Student {

    private Long id;

    private String name;

    private int age;

    private String describtion;

    private Date date;

    public Student(Long id, String name, int age, String describtion, Date date) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.describtion = describtion;
        this.date = date;
    }
}
