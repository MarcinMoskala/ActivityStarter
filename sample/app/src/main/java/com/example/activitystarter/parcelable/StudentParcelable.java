package com.example.activitystarter.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentParcelable implements Parcelable {

    private int id;
    private String name;
    private char grade;

    // Constructor
    public StudentParcelable(int id, String name, char grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    // Getter and setter methods
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

    @Override
    public String toString() {
        return "{id:" + id + ", name: " + name + ", grade: " + grade + "}";
    }

    // Parcelling part
    public StudentParcelable(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.grade = data[2].charAt(0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                "" + this.id,
                this.name,
                "" + this.grade});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StudentParcelable createFromParcel(Parcel in) {
            return new StudentParcelable(in);
        }

        public StudentParcelable[] newArray(int size) {
            return new StudentParcelable[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentParcelable that = (StudentParcelable) o;

        if (id != that.id) return false;
        if (grade != that.grade) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) grade;
        return result;
    }
}