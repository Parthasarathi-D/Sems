/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/coursedetail")
public class coursedetail extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");// Get the student ID from session or wherever it's stored

            String query = "SELECT cd.course_id, cd.course_name, cd.course_credits, cd.course_dept, cd.teaching_faculty " +
"FROM " + studentid + "_course ic " +
"JOIN course_details cd ON ic.course_id = cd.course_id ";

            PreparedStatement stmt = con.prepareStatement(query);
            
            ResultSet rs = stmt.executeQuery();

            ArrayList<String[]> enrolledCourses = new ArrayList<>();

            while (rs.next()) {
                String[] course = new String[5]; // Array to store each course's details
                course[0] = String.valueOf(rs.getInt("course_id"));
                course[1] = rs.getString("course_name");
                course[2] = String.valueOf(rs.getInt("course_credits"));
                course[3] = rs.getString("course_dept");
                course[4] = rs.getString("teaching_faculty");
                enrolledCourses.add(course);
            }

            con.close();

            request.setAttribute("enrolledCourses", enrolledCourses);
            RequestDispatcher dispatcher = request.getRequestDispatcher("enrolledcourse.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getStudentId() {
        // Logic to retrieve the student ID from the session or other sources
        return 123; // Replace with your actual student ID retrieval mechanism
    }
}
