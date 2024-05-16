/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        

        // Hash the entered password to match with the stored hashed password in the database
        String hashedPassword = hashPassword(password);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM students WHERE username = ?");
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
               long phoneNumber = rs.getLong("phone_number");
                String userId = String.valueOf(phoneNumber);

 // Assuming the column name for user ID is "id"

                // Compare hashed passwords
                if (storedHashedPassword.equals(hashedPassword)) {
                    // Successful login - Create a session and store user ID
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", userId);

                    response.sendRedirect("dashboard.jsp"); // Redirect to dashboard upon successful login
                } else {
                    // Incorrect password
                    response.sendRedirect("login.jsp?error=invalid");
                }
            } else {
                // User not found
                response.sendRedirect("login.jsp?error=notfound");
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
}
