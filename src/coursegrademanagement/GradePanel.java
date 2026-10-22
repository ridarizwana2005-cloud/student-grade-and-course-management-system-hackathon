
package coursegrademanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class GradePanel extends JPanel {

    private JTextField enrollmentField;
    private JTextField marksField;
    private JLabel resultLabel;

    private GradeCalculator calculator;
    private GradeDAO gradeDAO;

    public GradePanel() {
        calculator = new GradeCalculator(new StandardGradePolicy());
        gradeDAO = new GradeDAO();

        setLayout(new GridLayout(5, 2, 10, 10));

        enrollmentField = new JTextField();
        marksField = new JTextField();
        resultLabel = new JLabel("Grade: ");

        JButton calculateButton = new JButton("Calculate Grade");
        JButton saveButton = new JButton("Save Grade");
        JButton retrieveButton = new JButton("Retrieve Grade");

        add(new JLabel("Enrollment ID:"));
        add(enrollmentField);

        add(new JLabel("Marks (0-100):"));
        add(marksField);

        add(calculateButton);
        add(saveButton);

        add(retrieveButton);
        add(resultLabel);

        calculateButton.addActionListener(e -> calculateGrade());
        saveButton.addActionListener(e -> saveGrade());
        retrieveButton.addActionListener(e -> retrieveGrade());
    }

    private void calculateGrade() {
        try {
            int enrollmentId =
                    Integer.parseInt(enrollmentField.getText().trim());

            double marks =
                    Double.parseDouble(marksField.getText().trim());

            if (enrollmentId <= 0 || marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a positive enrollment ID and marks from 0 to 100."
                );
                return;
            }

            String grade = calculator.calculateGrade(marks);
            resultLabel.setText("Grade: " + grade);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid enrollment ID and numeric marks."
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void saveGrade() {
        try {
            int enrollmentId =
                    Integer.parseInt(enrollmentField.getText().trim());

            double marks =
                    Double.parseDouble(marksField.getText().trim());

            if (enrollmentId <= 0 || marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a positive enrollment ID and marks from 0 to 100."
                );
                return;
            }

            String grade = calculator.calculateGrade(marks);

            if (gradeDAO.saveGrade(enrollmentId, marks, grade)) {
                resultLabel.setText("Grade: " + grade);
                JOptionPane.showMessageDialog(
                        this, "Grade saved successfully!"
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid enrollment ID and marks."
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save grade. Check that the enrollment ID exists.\n"
                            + ex.getMessage()
            );
        }
    }

    private void retrieveGrade() {
        try {
            int enrollmentId =
                    Integer.parseInt(enrollmentField.getText().trim());

            if (enrollmentId <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Enter a positive enrollment ID."
                );
                return;
            }

            Grade grade = gradeDAO.getGrade(enrollmentId);

            if (grade != null) {
                marksField.setText(String.valueOf(grade.getMarks()));
                resultLabel.setText("Grade: " + grade.getGrade());
            } else {
                resultLabel.setText("Grade: Not found");
                JOptionPane.showMessageDialog(
                        this, "No grade found for this enrollment."
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid enrollment ID."
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this, "Could not retrieve grade.\n" + ex.getMessage()
            );
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grade Management");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new GradePanel());
            frame.setSize(550, 250);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}