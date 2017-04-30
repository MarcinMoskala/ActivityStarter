package activitystarter.generation.converters;

public class StudentParcelerWithoutAnnotation {

    private int id;
    private String name;
    private char grade;

    public StudentParcelerWithoutAnnotation() {
    }

    // Constructor
    public StudentParcelerWithoutAnnotation(int id, String name, char grade) {
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