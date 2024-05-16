/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AttendanceDetailsServlet")
public class attendancedata extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT a.course_id, c.course_name, a.total_class, a.attended_class, a.percentage FROM "+studentid+"_attendance a JOIN course_details c ON a.course_id = c.course_id");

          
ArrayList<String[]> enrolledCourses = new ArrayList<>();

while (rs.next()) {
    String[] course = new String[5]; // Array to store each course's details
    course[0] = String.valueOf(rs.getInt("course_id"));
    course[1] = rs.getString("course_name");
    course[2] = String.valueOf(rs.getInt("total_class"));
    course[3] = rs.getObject("attended_class") != null ? String.valueOf(rs.getInt("attended_class")) : "-1";
    course[4] = rs.getObject("percentage") != null ? String.valueOf(rs.getInt("percentage")) : "-1";
    enrolledCourses.add(course);
}

request.setAttribute("enrolledCourses", enrolledCourses);


            request.getRequestDispatcher("attendance.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
