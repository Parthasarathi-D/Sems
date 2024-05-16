import jakarta.servlet.RequestDispatcher;
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
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet("/FetchCourse")
public class fetchcourse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String studentid = (String) session.getAttribute("userId");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");
            PreparedStatement stmt = con.prepareStatement("SELECT cd.course_id, cd.course_name, cd.course_credits, cd.course_dept, cd.teaching_faculty\n" +
"FROM course_details cd\n" +
"LEFT JOIN "+studentid+"_course ic ON cd.course_id = ic.course_id\n" +
"WHERE ic.course_id IS NULL;");
            ResultSet rs = stmt.executeQuery();

            ArrayList<String[]> courses = new ArrayList<>(); // ArrayList to store course details

            while (rs.next()) {
                String[] course = new String[5]; // Array to store each course's details
                course[0] = String.valueOf(rs.getInt("course_id"));
                course[1] = rs.getString("course_name");
                course[2] = String.valueOf(rs.getInt("course_credits"));
                course[3] = rs.getString("course_dept");
                course[4] = rs.getString("teaching_faculty");
                courses.add(course);
            }

            con.close();

            request.setAttribute("courses", courses); // Set courses as an attribute in the request
            RequestDispatcher dispatcher = request.getRequestDispatcher("courseregister.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
