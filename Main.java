import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    // MySQL Database connection settings
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Leave empty if no password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println(" Welcome to Employee Management System (MySQL) ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add New Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Search Employee by ID");
            System.out.println("4. Delete Employee by ID");
            System.out.println("5. Update Employee by ID");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Employee ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter Employee Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Employee Salary: ");
                        double salary = scanner.nextDouble();

                        String insertQuery = "INSERT INTO employees (id, name, salary) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                            pstmt.setInt(1, id);
                            pstmt.setString(2, name);
                            pstmt.setDouble(3, salary);
                            pstmt.executeUpdate();
                            System.out.println("Employee added and saved to MySQL database successfully!");
                        }
                        break;

                    case 2:
                        String selectQuery = "SELECT * FROM employees";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectQuery)) {
                            
                            boolean found = false;
                            System.out.println("\n--- EMPLOYEE LIST FROM DATABASE ---");
                            while (rs.next()) {
                                found = true;
                                int empId = rs.getInt("id");
                                String empName = rs.getString("name");
                                double empSalary = rs.getDouble("salary");
                                
                                Employee emp = new Employee(empId, empName, empSalary);
                                emp.displayDetails();
                                System.out.println("---------------------");
                            }
                            if (!found) {
                                System.out.println("No employees found in the database!");
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter Employee ID to search: ");
                        int searchId = scanner.nextInt();

                        String searchQuery = "SELECT * FROM employees WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
                            pstmt.setInt(1, searchId);
                            try (ResultSet rs = pstmt.executeQuery()) {
                                if (rs.next()) {
                                    System.out.println("\n--- EMPLOYEE FOUND ---");
                                    int empId = rs.getInt("id");
                                    String empName = rs.getString("name");
                                    double empSalary = rs.getDouble("salary");
                                    
                                    Employee emp = new Employee(empId, empName, empSalary);
                                    emp.displayDetails();
                                    System.out.println("----------------------");
                                } else {
                                    System.out.println("Employee with ID " + searchId + " not found!");
                                }
                            }
                        }
                        break;

                    case 4:
                        System.out.print("Enter Employee ID to delete: ");
                        int deleteId = scanner.nextInt();

                        String deleteQuery = "DELETE FROM employees WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                            pstmt.setInt(1, deleteId);
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Employee with ID " + deleteId + " deleted successfully!");
                            } else {
                                System.out.println("Employee with ID " + deleteId + " not found!");
                            }
                        }
                        break;

                    case 5:
                        System.out.print("Enter Employee ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter new Name: ");
                        String newName = scanner.nextLine();

                        System.out.print("Enter new Salary: ");
                        double newSalary = scanner.nextDouble();

                        String updateQuery = "UPDATE employees SET name = ?, salary = ? WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                            pstmt.setString(1, newName);
                            pstmt.setDouble(2, newSalary);
                            pstmt.setInt(3, updateId);
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Employee updated successfully in database!");
                            } else {
                                System.out.println("Employee with ID " + updateId + " not found!");
                            }
                        }
                        break;

                    case 6:
                        running = false;
                        System.out.println("Thank you for using Employee Management System!");
                        break;

                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 6.");
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}