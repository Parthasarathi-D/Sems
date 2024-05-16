/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProfileManagementServlet")
public class profilemanage extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");
        String username = "";
        String email = "";
        String address = "";
       // String dobStr = ""; // String representation of date of birth
       // Date dob = null; // Date object for date of birth

        // Fetch user profile details from the database
        try {
            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            // Prepare SQL statement
            String query = "SELECT username, email, address FROM students WHERE phone_number="+studentid;
            PreparedStatement pstmt = con.prepareStatement(query);
            
            // Execute query
            ResultSet rs = pstmt.executeQuery();

            // Process the result set
            if (rs.next()) {
                username = rs.getString("username");
                email = rs.getString("email");
                address = rs.getString("address");
               // dobStr = rs.getString("dob");

                // Convert String DOB to Date object
                //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
               // dob = dateFormat.parse(dobStr);
            }

            // Close connections
            rs.close();
            pstmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set retrieved profile details as request attributes
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("address", address);

        // Forward to the profile.jsp page
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}
