
package coursegrademanagement;

public class StudentDAOTest {

    public static void main(String[] args) {

        Student student = new Student(
            101,
            "Anu Krishna",
            "CSE",
            3,
            "anu@example.com"
        );

        StudentDAO dao = new StudentDAO();

        if (dao.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Failed to add student.");
        }
    }
}
