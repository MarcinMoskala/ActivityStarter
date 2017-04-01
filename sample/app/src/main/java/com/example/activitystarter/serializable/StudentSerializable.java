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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentSerializable that = (StudentSerializable) o;

        if (id != that.id) return false;
        if (grade != that.grade) return false;
        if (passing != that.passing) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) grade;
        result = 31 * result + (passing ? 1 : 0);
        return result;
    }
}