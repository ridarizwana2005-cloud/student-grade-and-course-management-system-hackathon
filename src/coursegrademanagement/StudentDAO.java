
package coursegrademanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 1. Add a student
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students "
                   + "(student_id, name, department, semester, email) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getDepartment());
            ps.setInt(4, student.getSemester());
            ps.setString(5, student.getEmail());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // 2. Retrieve all students
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getInt("semester"),
                    rs.getString("email")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println(
                "Error retrieving students: " + e.getMessage()
            );
        }

        return students;
    }

    // 3. Update an existing student
    public boolean updateStudent(Student student) {

        String sql = "UPDATE students SET name = ?, "
                   + "department = ?, semester = ?, email = ? "
                   + "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.setInt(3, student.getSemester());
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getStudentId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println(
                "Error updating student: " + e.getMessage()
            );
            return false;
        }
    }

    // 4. Delete a student using student ID
    public boolean deleteStudent(int studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println(
                "Error deleting student: " + e.getMessage()
            );
            return false;
        }
    }
}