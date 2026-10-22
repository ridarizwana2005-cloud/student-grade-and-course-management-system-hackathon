
package coursegrademanagement;

import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class PrerequisitePanel extends JFrame {

    private JComboBox<String> courseCombo;
    private JComboBox<String> prerequisiteCombo;
    private DefaultTableModel tableModel;

    private final CourseDAO courseDAO = new CourseDAO();
    private final PrerequisiteDAO prerequisiteDAO =
            new PrerequisiteDAO();

    public PrerequisitePanel() {
        setTitle("Prerequisite Management");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        courseCombo = new JComboBox<>();
        prerequisiteCombo = new JComboBox<>();

        JPanel inputPanel = new JPanel(new FlowLayout());

        inputPanel.add(new JLabel("Course:"));
        inputPanel.add(courseCombo);
        inputPanel.add(new JLabel("Prerequisite:"));
        inputPanel.add(prerequisiteCombo);

        JButton addButton = new JButton("Add Prerequisite");
        JButton refreshButton = new JButton("Refresh");
        JButton clearButton = new JButton("Clear");

        inputPanel.add(addButton);
        inputPanel.add(refreshButton);
        inputPanel.add(clearButton);

        tableModel = new DefaultTableModel(
                new String[]{
                    "Course ID",
                    "Course Name",
                    "Prerequisite ID",
                    "Prerequisite Name"
                }, 0);

        JTable table = new JTable(tableModel);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadCourses();
        refreshTable();

        addButton.addActionListener(e -> addPrerequisite());
        refreshButton.addActionListener(e -> refreshTable());

        clearButton.addActionListener(e -> {
            courseCombo.setSelectedIndex(-1);
            prerequisiteCombo.setSelectedIndex(-1);
        });
    }

    private void loadCourses() {
        try {
            List<Course> courses = courseDAO.getAllCourses();

            courseCombo.removeAllItems();
            prerequisiteCombo.removeAllItems();

            for (Course course : courses) {
                String item = course.getCourseId()
                        + " - " + course.getCourseName();

                courseCombo.addItem(item);
                prerequisiteCombo.addItem(item);
            }

            courseCombo.setSelectedIndex(-1);
            prerequisiteCombo.setSelectedIndex(-1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load courses: " + ex.getMessage());
        }
    }

    private void addPrerequisite() {
        String selectedCourse =
                (String) courseCombo.getSelectedItem();

        String selectedPrerequisite =
                (String) prerequisiteCombo.getSelectedItem();

        if (selectedCourse == null || selectedPrerequisite == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both courses.");
            return;
        }

        String courseId = selectedCourse.split(" - ", 2)[0];
        String prerequisiteId =
                selectedPrerequisite.split(" - ", 2)[0];

        if (courseId.equals(prerequisiteId)) {
            JOptionPane.showMessageDialog(this,
                    "A course cannot be its own prerequisite.");
            return;
        }

        try {
            prerequisiteDAO.addPrerequisite(
                    courseId, prerequisiteId);

            JOptionPane.showMessageDialog(this,
                    "Prerequisite added successfully!");

            refreshTable();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not add prerequisite: "
                    + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        try {
            for (String[] row
                    : prerequisiteDAO.getAllPrerequisites()) {
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load prerequisites: "
                    + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrerequisitePanel().setVisible(true);
        });
    }
}
