package coursegrademanagement;
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private String courseId;
    public Enrollment(int enrollmentId, int studentId, String courseId) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
    }
    public int getEnrollmentId() {
        return enrollmentId;
    }
    public int getStudentId() {
        return studentId;
    }
    public String getCourseId() {
        return courseId;
    }
}