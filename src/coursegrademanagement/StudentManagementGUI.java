
package coursegrademanagement;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementGUI extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField departmentField;
    private JTextField semesterField;
    private JTextField emailField;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private StudentDAO dao = new StudentDAO();

    public StudentManagementGUI() {

        setTitle("Student Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Student form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));

        idField = new JTextField();
        nameField = new JTextField();
        departmentField = new JTextField();
        semesterField = new JTextField();
        emailField = new JTextField();

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);

        formPanel.add(new JLabel("Semester:"));
        formPanel.add(semesterField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{
                "Student ID", "Name", "Department",
                "Semester", "Email"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Buttons
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton clearButton = new JButton("Clear Fields");
        JButton refreshButton = new JButton("Refresh Table");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add student
        addButton.addActionListener(e -> addStudent());

        // Update student
        updateButton.addActionListener(e -> updateStudent());

        // Delete student
        deleteButton.addActionListener(e -> deleteStudent());

        // Clear fields
        clearButton.addActionListener(e -> clearFields());

        // Refresh table
        refreshButton.addActionListener(e -> loadStudents());

        // Select a table row to fill the form
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && studentTable.getSelectedRow() != -1) {

                int row = studentTable.getSelectedRow();

                idField.setText(
                    tableModel.getValueAt(row, 0).toString()
                );
                nameField.setText(
                    tableModel.getValueAt(row, 1).toString()
                );
                departmentField.setText(
                    tableModel.getValueAt(row, 2).toString()
                );
                semesterField.setText(
                    tableModel.getValueAt(row, 3).toString()
                );

                Object email = tableModel.getValueAt(row, 4);
                emailField.setText(email == null ? "" : email.toString());
            }
        });

        loadStudents();
    }

    private void addStudent() {

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String department = departmentField.getText().trim();
            int semester = Integer.parseInt(
                semesterField.getText().trim()
            );
            String email = emailField.getText().trim();

            if (name.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Enter the student's name and department."
                );
                return;
            }

            if (semester < 1) {
                JOptionPane.showMessageDialog(
                    this, "Semester must be a positive number."
                );
                return;
            }

            Student student = new Student(
                id, name, department, semester,
                email.isEmpty() ? null : email
            );

            if (dao.addStudent(student)) {
                JOptionPane.showMessageDialog(
                    this, "Student added successfully!"
                );
                loadStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Could not add student. Check the ID and email."
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Student ID and semester must be numbers."
            );
        }
    }

    private void updateStudent() {

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String department = departmentField.getText().trim();
            int semester = Integer.parseInt(
                semesterField.getText().trim()
            );
            String email = emailField.getText().trim();

            if (name.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Enter the student's name and department."
                );
                return;
            }

            if (semester < 1) {
                JOptionPane.showMessageDialog(
                    this, "Semester must be a positive number."
                );
                return;
            }

            Student student = new Student(
                id, name, department, semester,
                email.isEmpty() ? null : email
            );

            if (dao.updateStudent(student)) {
                JOptionPane.showMessageDialog(
                    this, "Student updated successfully!"
                );
                loadStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Student not updated. Check the student ID."
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Student ID and semester must be numbers."
            );
        }
    }

    private void deleteStudent() {

        try {
            int id = Integer.parseInt(idField.getText().trim());

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Permanently delete student ID " + id + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                if (dao.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(
                        this, "Student deleted successfully!"
                    );
                    loadStudents();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(
                        this, "Student not found or deletion failed."
                    );
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Enter or select a valid student ID."
            );
        }
    }

    private void loadStudents() {

        tableModel.setRowCount(0);

        List<Student> students = dao.getAllStudents();

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getStudentId(),
                student.getName(),
                student.getDepartment(),
                student.getSemester(),
                student.getEmail()
            });
        }
    }

    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        departmentField.setText("");
        semesterField.setText("");
        emailField.setText("");

        studentTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentManagementGUI().setVisible(true);
        });
    }
}