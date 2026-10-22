package coursegrademanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void addCourse(Course course) throws SQLException {

        String sql = "INSERT INTO courses "
                   + "(course_id, course_name, credits, course_type) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, course.getCourseId());
            ps.setString(2, course.getCourseName());
            ps.setInt(3, course.getCredits());

            if (course instanceof LabCourse) {
                ps.setString(4, "LAB");
            } else {
                ps.setString(4, "THEORY");
            }

            ps.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {

        List<Course> courses = new ArrayList<>();

        String sql = "SELECT course_id, course_name, credits, course_type "
                   + "FROM courses";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = rs.getString("course_id");
                String name = rs.getString("course_name");
                int credits = rs.getInt("credits");
                String type = rs.getString("course_type");

                if ("LAB".equalsIgnoreCase(type)) {
                    courses.add(new LabCourse(id, name, credits));
                } else {
                    courses.add(new TheoryCourse(id, name, credits));
                }
            }
        }

        return courses;
    }

    public boolean isEnrolled(int studentId, String courseId)
            throws SQLException {

        String sql = "SELECT 1 FROM enrollments "
                   + "WHERE student_id = ? AND course_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Checks whether all prerequisites have been passed.
    public boolean hasCompletedPrerequisites(
            int studentId, String courseId) throws SQLException {

        String sql =
            "SELECT COUNT(*) "
          + "FROM prerequisites p "
          + "WHERE p.course_id = ? "
          + "AND NOT EXISTS ("
          + "    SELECT 1 "
          + "    FROM enrollments e "
          + "    JOIN grades g "
          + "      ON e.enrollment_id = g.enrollment_id "
          + "    WHERE e.student_id = ? "
          + "      AND e.course_id = p.prerequisite_course_id "
          + "      AND UPPER(TRIM(g.grade)) IN "
          + "          ('A+', 'A', 'B', 'C', 'D', 'E', "
          + "           'PASS', 'DISTINCTION')"
          + ")";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    public void enrollStudent(int studentId, String courseId)
            throws SQLException {

        // Prevent duplicate enrollment.
        if (isEnrolled(studentId, courseId)) {
            throw new SQLException(
                "Student is already enrolled in this course."
            );
        }

        // Prevent enrollment if prerequisites are incomplete.
        if (!hasCompletedPrerequisites(studentId, courseId)) {
            throw new SQLException(
                "Enrollment blocked: Student has not successfully "
              + "completed all prerequisite courses."
            );
        }

        String sql = "INSERT INTO enrollments (student_id, course_id) "
                   + "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();
        }
    }

    public List<Course> getCoursesForStudent(int studentId)
            throws SQLException {

        List<Course> courses = new ArrayList<>();

        String sql = "SELECT c.course_id, c.course_name, "
                   + "c.credits, c.course_type "
                   + "FROM courses c "
                   + "JOIN enrollments e ON c.course_id = e.course_id "
                   + "WHERE e.student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String id = rs.getString("course_id");
                    String name = rs.getString("course_name");
                    int credits = rs.getInt("credits");
                    String type = rs.getString("course_type");

                    if ("LAB".equalsIgnoreCase(type)) {
                        courses.add(new LabCourse(id, name, credits));
                    } else {
                        courses.add(new TheoryCourse(id, name, credits));
                    }
                }
            }
        }

        return courses;
    }
}
