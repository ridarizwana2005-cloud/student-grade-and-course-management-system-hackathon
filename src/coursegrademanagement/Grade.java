package coursegrademanagement;
public class Grade {
    private int enrollmentId;
    private double marks;
    private String grade;
    public Grade(int enrollmentId, double marks, String grade) {
        this.enrollmentId = enrollmentId;
        this.marks = marks;
        this.grade = grade;
    }
    public int getEnrollmentId() {
        return enrollmentId;
    }
    public double getMarks() {
        return marks;
    }
    public String getGrade() {
        return grade;
    }
    public void setMarks(double marks) {
        this.marks = marks;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
}