package com.marcinmoskala.kotlinapp;

import java.io.Serializable;

public class StudentSerializable implements Serializable {

    int id;
    String name;
    char grade;
    boolean isPassing;

    public StudentSerializable(int id, String name, char grade, boolean isPassing) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.isPassing = isPassing;
    }
}