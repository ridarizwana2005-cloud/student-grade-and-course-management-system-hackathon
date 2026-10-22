
package coursegrademanagement;

public class Student {

    private int studentId;
    private String name;
    private String department;
    private int semester;
    private String email;

    public Student(int studentId, String name,
                   String department, int semester,
                   String email) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.semester = semester;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }
    public int getSemester() {
        return semester;
    }
    public String getEmail() {
        return email;
    }
}
