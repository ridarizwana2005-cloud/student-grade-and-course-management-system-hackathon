
package coursegrademanagement;

public class DeleteStudentTest {

    public static void main(String[] args) {

        StudentDAO dao = new StudentDAO();

        int testStudentId = 999;

        if (dao.addStudent(new Student(
                testStudentId,
                "Temporary Test Student",
                "CSE",
                3,
                "temporary999@example.com"))) {

            System.out.println("Temporary student added.");

            if (dao.deleteStudent(testStudentId)) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student deletion failed.");
            }

        } else {
            System.out.println(
                "Could not add test student. Check whether ID 999 "
                + "or the test email already exists."
            );
        }
    }
}