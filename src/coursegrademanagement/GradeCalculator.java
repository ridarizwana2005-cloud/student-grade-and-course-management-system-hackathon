
package coursegrademanagement;

public class GradeCalculator {

    private StandardGradePolicy policy;

    public GradeCalculator(StandardGradePolicy policy) {
        this.policy = policy;
    }

    public String calculateGrade(double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException(
                "Marks must be between 0 and 100."
            );
        }

        return policy.calculateGrade(marks);
    }
}