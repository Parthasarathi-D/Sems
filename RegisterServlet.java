/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Parthasarathi D
 */
 import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        String dob = request.getParameter("dob");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");

        // Hash the password
        String hashedPassword = hashPassword(password);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            // Create tables for the student dynamically
            createTables(con, phone);

            // Insert student details into the main students table
            PreparedStatement ps = con.prepareStatement("INSERT INTO students (name, email, dept, date_of_birth, address, phone_number, gender, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setString(4, dob);
            ps.setString(5, address);
            ps.setString(6, phone);
            ps.setString(7, gender);
            ps.setString(8, email); // Using email as the username (modify this as per your requirements)
            ps.setString(9, hashedPassword);

            int status = ps.executeUpdate();
            if (status > 0) {
                // Redirect to login page after successful registration, passing the username
                response.sendRedirect("login.jsp?username=" + phone);
            } else {
                // Handle registration failure
                response.sendRedirect("register.jsp");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to hash the password using SHA-256 algorithm
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create tables for the student
private void createTables(Connection con, String id) throws SQLException {
    // Create 'id_course' table for the student
    String createCourseTable = "CREATE TABLE " + id + "_course (course_id INT)"; // Define 'course_id' column
    PreparedStatement createCourseStmt = con.prepareStatement(createCourseTable);
    createCourseStmt.executeUpdate();

    // Create 'id_attendance' table for the student
    String createAttendanceTable = "CREATE TABLE " + id + "_attendance (course_id INT,total_class INT,attended_class INT, percentage INT)"; // Define 'course_id' column
    PreparedStatement createAttendanceStmt = con.prepareStatement(createAttendanceTable);
    createAttendanceStmt.executeUpdate();

    // Create 'id_mark' table for the student
    String createMarkTable = "CREATE TABLE " + id + "_mark (course_id INT, assess_1_grade DECIMAL(5,2), assess_2_grade DECIMAL(5,2), final_grade varchar(3))";
    PreparedStatement createMarkStmt = con.prepareStatement(createMarkTable);
    createMarkStmt.executeUpdate();
    
}


}
