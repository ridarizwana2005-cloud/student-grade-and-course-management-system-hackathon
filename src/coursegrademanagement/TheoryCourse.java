package coursegrademanagement;
public class TheoryCourse extends Course {
    public TheoryCourse(String courseId, String courseName, int credits) {
        super(courseId, courseName, credits);
    }
    @Override
    public String calculateGrade(double marks){
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }
}