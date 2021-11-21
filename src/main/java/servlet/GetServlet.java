package servlet;

import entity.Product;
import server.DefaultServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws   IOException {
        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html;charset=cp1251");
        System.out.println(response.getCharacterEncoding() + "Товар");
        response.getWriter().println("<html><head></head><body>\n");
        String headlines = "<table><tr><th>ID</th><th>Product</th><th>Company</th><th>Quantity</th></tr>\n";
        response.getWriter().println(headlines);
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "123567");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM \"product\"");
                while (rs.next()) {
                    response.getWriter().println("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td></tr>\n");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        response.getWriter().println("</body></html>");
    }
}
