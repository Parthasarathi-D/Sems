
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

@WebServlet("/registercourses")
public class registercourses extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String[] selectedCourses = request.getParameterValues("selectedCourses");
        HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");
// Get the logged-in student's email (modify this based on your session handling)
            System.out.println("Session attribute value: " + studentid);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");

            // Insert selected courses into id_course table
            for (String courseId : selectedCourses) {
                insertCourseDetails(con, studentid, courseId);
                insertAttendanceDetails(con, studentid, courseId);
                insertMarkDetails(con, studentid, courseId);
            }

            con.close();
            response.sendRedirect("success.jsp"); // Redirect to success page upon successful registration

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to error page if an exception occurs
        }
    }

    // Method to insert course details
   private void insertCourseDetails(Connection con, String studentEmail, String courseId) throws SQLException {
    String tableName=studentEmail+"_course";
    String insertQuery = "INSERT INTO " + tableName + " ( course_id) VALUES ( ?)";
    PreparedStatement idCourseStmt = con.prepareStatement(insertQuery);
   
    idCourseStmt.setInt(1, Integer.parseInt(courseId));
    idCourseStmt.executeUpdate();
}


    // Method to insert attendance details
   private void insertAttendanceDetails(Connection con, String studentEmail, String courseId) throws SQLException {
        String tableName=studentEmail+"_Attendance";
    PreparedStatement attendanceStmt = con.prepareStatement("INSERT INTO " + tableName + "(course_id, total_class) VALUES (?, ?)");
    attendanceStmt.setInt(1, Integer.parseInt(courseId));
    attendanceStmt.setInt(2,45);
    attendanceStmt.executeUpdate();
}


    // Method to insert mark details
   private void insertMarkDetails(Connection con, String studentEmail, String courseId) throws SQLException {
        String tableName=studentEmail+"_mark";
    PreparedStatement marksStmt = con.prepareStatement("INSERT INTO " + tableName + " (course_id) VALUES (?)");
    marksStmt.setInt(1, Integer.parseInt(courseId));
    marksStmt.executeUpdate();
}

}
