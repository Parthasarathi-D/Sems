/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
     //   String dob = request.getParameter("dob"); // Get updated date of birth from the form

        // Perform validation and update operations
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            PreparedStatement ps = con.prepareStatement("UPDATE students SET username=?, email=?, address=?   WHERE phone_number="+studentid);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, address);
        //    ps.setString(4, dob);

            int status = ps.executeUpdate();
            if (status > 0) {
                // If the update is successful, redirect to the profile page
                response.sendRedirect("profilemanage");
            } else {
                // Handle update failure
                // You might want to forward to an error page or handle it as needed
                response.sendRedirect("updateProfile.jsp");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
