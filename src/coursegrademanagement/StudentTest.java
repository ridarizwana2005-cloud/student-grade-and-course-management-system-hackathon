
package coursegrademanagement;

public class StudentTest {

    public static void main(String[] args) {

        Student s1 = new Student(
            101,
            "Anu Krishna",
            "CSE",
            3,
            "anu@example.com"
        );

        System.out.println("Student Details");
        System.out.println("--------------------");
        System.out.println("Student ID: " + s1.getStudentId());
        System.out.println("Name: " + s1.getName());
        System.out.println("Department: " + s1.getDepartment());
        System.out.println("Semester: " + s1.getSemester());
        System.out.println("Email: " + s1.getEmail());
    }
}
