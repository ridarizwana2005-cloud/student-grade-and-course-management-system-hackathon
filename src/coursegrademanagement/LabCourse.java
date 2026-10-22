package coursegrademanagement;
public class LabCourse extends Course {
    public LabCourse(String id, String name, int credits) {
        super(id, name, credits);
    }
    @Override
    public String calculateGrade(double marks) {
        if (marks >= 75) return "Distinction";
        if (marks >= 40) return "Pass";

        return "Fail";
    }
}