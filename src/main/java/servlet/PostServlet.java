package servlet;

import entity.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        Product product = new Product();
        product.setName(req.getParameter("name"));
        product.setCompany("defaultCompany");
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));

        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "123567");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"product\"(name,company,quantity) values (?,?,?)");
            statement.setString(1,product.getName());
            statement.setString(2, product.getCompany());
            statement.setInt(3, product.getQuantity());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/").forward(req,resp);
    }
}
