
package coursegrademanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrerequisiteDAO {

    // Add a prerequisite for a course
    public void addPrerequisite(
            String courseId, String prerequisiteCourseId)
            throws SQLException {

        if (courseId.equalsIgnoreCase(prerequisiteCourseId)) {
            throw new SQLException(
                    "A course cannot be its own prerequisite.");
        }

        String sql = "INSERT INTO prerequisites "
                + "(course_id, prerequisite_course_id) "
                + "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.setString(2, prerequisiteCourseId);
            ps.executeUpdate();
        }
    }

    // Retrieve all prerequisite relationships
    public List<String[]> getAllPrerequisites()
            throws SQLException {

        List<String[]> prerequisites = new ArrayList<>();

        String sql = "SELECT p.course_id, c1.course_name, "
                + "p.prerequisite_course_id, c2.course_name "
                + "FROM prerequisites p "
                + "JOIN courses c1 ON p.course_id = c1.course_id "
                + "JOIN courses c2 "
                + "ON p.prerequisite_course_id = c2.course_id "
                + "ORDER BY p.course_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] row = {
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getString("prerequisite_course_id"),
                    rs.getString(4)
                };

                prerequisites.add(row);
            }
        }

        return prerequisites;
    }
}