package com.example.activitystarter.parceler;

@org.parceler.Parcel
public class StudentParceler {

    private int id;
    private String name;
    private char grade;

    public StudentParceler() {
    }

    // Constructor
    public StudentParceler(int id, String name, char grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGrade() {
        return grade;
    }

    public void setGrade(char grade) {
        this.grade = grade;
    }
}