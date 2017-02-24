package com.example.activitystarter.serializable;

import java.io.Serializable;

public class StudentSerializable implements Serializable {

    public int id;
    public String name;
    public char grade;
    public boolean passing;

    public StudentSerializable(int id, String name, char grade, boolean passing) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.passing = passing;
    }
}