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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MarkDetailsServlet")
public class markdetail extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String studentId = (String) session.getAttribute("userId");

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sems", "root", "");
            PreparedStatement stmt = con.prepareStatement("SELECT m.course_id, m.assess_1_grade, m.assess_2_grade, m.final_grade, c.course_name FROM "+studentId+"_mark m JOIN course_details c ON m.course_id = c.course_id");
            ResultSet rs = stmt.executeQuery();

            ArrayList<String[]> markDetails = new ArrayList<>();
            while (rs.next()) {
                String[] mark = new String[5];
                mark[0] = String.valueOf(rs.getInt("course_id"));
                mark[1] = rs.getString("course_name");
                mark[2] = rs.getObject("assess_1_grade") != null ? String.valueOf(rs.getFloat("assess_1_grade")) : "N/A";
                mark[3] = rs.getObject("assess_2_grade") != null ? String.valueOf(rs.getFloat("assess_2_grade")) : "N/A";
                mark[4] = rs.getString("final_grade") != null ? rs.getString("final_grade") : "N/A";
                markDetails.add(mark);
            }
            con.close();

            request.setAttribute("markDetails", markDetails);
            request.getRequestDispatcher("mark.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
