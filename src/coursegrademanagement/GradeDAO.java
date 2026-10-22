
package coursegrademanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeDAO {

    public boolean saveGrade(int enrollmentId, double marks, String grade)
            throws SQLException {

        String sql = "INSERT INTO grades (enrollment_id, marks, grade) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE marks = ?, grade = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ps.setDouble(2, marks);
            ps.setString(3, grade);
            ps.setDouble(4, marks);
            ps.setString(5, grade);

            return ps.executeUpdate() > 0;
        }
    }

    public Grade getGrade(int enrollmentId) throws SQLException {

        String sql = "SELECT enrollment_id, marks, grade "
                   + "FROM grades WHERE enrollment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Grade(
                        rs.getInt("enrollment_id"),
                        rs.getDouble("marks"),
                        rs.getString("grade")
                    );
                }
            }
        }

        return null;
    }
}
