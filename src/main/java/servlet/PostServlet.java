package servlet;

import DAO.ProductDAO;
import entity.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("cp1251");
        resp.setContentType("text/html;charset=cp1251");
        try{
            PagePost(req, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        }catch (NullPointerException nullException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters");
        } catch (SQLException throwables) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
    public void PagePost(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String name = req.getParameter("name");
        String company = req.getParameter("company");
        String quantityString = req.getParameter("quantity");

        if ((name == null)|(company == null)|(quantityString == null)){
            throw new NullPointerException();
        }
        int quantity = Integer.parseInt(quantityString);
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db",
                "postgres", "123567");
        ProductDAO productDAO = new ProductDAO(connection);
        productDAO.save(new Product(name,
                company,
                quantity));
    }
}
