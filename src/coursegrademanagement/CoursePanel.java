
package coursegrademanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CoursePanel extends JPanel {

    private JTextField courseIdField;
    private JTextField courseNameField;
    private JTextField creditsField;
    private JTextField studentIdField;
    private JComboBox<String> courseTypeBox;
    private DefaultTableModel tableModel;
    private JTable courseTable;
    private CourseDAO courseDAO;

    public CoursePanel() {
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));

        courseIdField = new JTextField();
        courseNameField = new JTextField();
        creditsField = new JTextField();
        studentIdField = new JTextField();

        courseTypeBox = new JComboBox<>(
                new String[]{"THEORY", "LAB"}
        );

        form.add(new JLabel("Course ID:"));
        form.add(courseIdField);
        form.add(new JLabel("Course Name:"));
        form.add(courseNameField);
        form.add(new JLabel("Credits:"));
        form.add(creditsField);
        form.add(new JLabel("Course Type:"));
        form.add(courseTypeBox);
        form.add(new JLabel("Student ID for Enrollment:"));
        form.add(studentIdField);

        JButton addButton = new JButton("Add Course");
        JButton enrollButton = new JButton("Enroll Student");
        JButton refreshButton = new JButton("Refresh Courses");
        JButton clearButton = new JButton("Clear Fields");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(addButton);
        buttons.add(enrollButton);
        buttons.add(refreshButton);
        buttons.add(clearButton);

        tableModel = new DefaultTableModel(
                new String[]{"Course ID", "Course Name", "Credits", "Type"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(courseTable), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addCourse());
        enrollButton.addActionListener(e -> enrollStudent());
        refreshButton.addActionListener(e -> refreshCourses());
        clearButton.addActionListener(e -> clearFields());

        refreshCourses();
    }

    private void addCourse() {
        try {
            String id = courseIdField.getText().trim();
            String name = courseNameField.getText().trim();
            String creditsText = creditsField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || creditsText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Fill in all course fields."
                );
                return;
            }

            int credits = Integer.parseInt(creditsText);

            if (credits <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Credits must be greater than zero."
                );
                return;
            }

            String type = (String) courseTypeBox.getSelectedItem();

            Course course;

            if ("LAB".equals(type)) {
                course = new LabCourse(id, name, credits);
            } else {
                course = new TheoryCourse(id, name, credits);
            }

            courseDAO.addCourse(course);

            JOptionPane.showMessageDialog(
                    this, "Course added successfully!"
            );

            clearFields();
            refreshCourses();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Credits must be a valid whole number."
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this, "Could not add course.\n" + ex.getMessage()
            );
        }
    }

    private void enrollStudent() {
        try {
            String studentText = studentIdField.getText().trim();
            String courseId = courseIdField.getText().trim();

            if (studentText.isEmpty() || courseId.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a Student ID and Course ID."
                );
                return;
            }

            int studentId = Integer.parseInt(studentText);

            if (studentId <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Student ID must be positive."
                );
                return;
            }

            courseDAO.enrollStudent(studentId, courseId);

            JOptionPane.showMessageDialog(
                    this, "Student enrolled successfully!"
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Student ID must be a valid whole number."
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this, "Could not enroll student.\n" + ex.getMessage()
            );
        }
    }

    private void refreshCourses() {
        try {
            tableModel.setRowCount(0);

            List<Course> courses = courseDAO.getAllCourses();

            for (Course course : courses) {
                String type = (course instanceof LabCourse)
                        ? "LAB" : "THEORY";

                tableModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredits(),
                    type
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this, "Could not load courses.\n" + ex.getMessage()
            );
        }
    }

    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        studentIdField.setText("");
        courseTypeBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Course Management");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new CoursePanel());
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}