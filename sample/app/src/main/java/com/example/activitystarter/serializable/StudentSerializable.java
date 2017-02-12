package com.example.activitystarter.serializable;

import java.io.Serializable;

public class StudentSerializable implements Serializable {

    int id;
    String name;
    char grade;
    boolean passing;

    public StudentSerializable(int id, String name, char grade, boolean passing) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.passing = passing;
    }
}