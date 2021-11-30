package servlet;

import DAO.ProductDAO;
import PageGeneration.PageGeneration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import commons.Connect;
import entity.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DBServlet extends HttpServlet {
    final Injector injector = Guice.createInjector();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws   IOException {
        ArrayList<String> productList = new ArrayList<String>();
        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html;charset=cp1251");
        try {
            Connection connection = Connect.connect();
            ProductDAO productDAO = new ProductDAO(connection);
            List<Product> rs = productDAO.all();
            for (Product product: rs) {
                productList.add("<tr><td>" + product.getId() +
                        "</td><td>" + product.getName() +
                        "</td><td>" + product.getCompany() +
                        "</td><td>" + product.getQuantity() + "</td></tr>\n");
            }
            response.getWriter().println(injector.getInstance(PageGeneration.class).PageGet(productList));
        } catch (SQLException throwables) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLException");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html;charset=cp1251");
        try{
            String name = request.getParameter("name");
            String company = request.getParameter("company");
            String quantityString = request.getParameter("quantity");

            if ((name == null)|(company == null)|(quantityString == null)){
                throw new NullPointerException();
            }
            int quantity = Integer.parseInt(quantityString);
            Connection connection = Connect.connect();
            ProductDAO productDAO = new ProductDAO(connection);
            productDAO.save(new Product(name,
                    company,
                    quantity));
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (NullPointerException nullException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters");
        } catch (SQLException throwables) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
}
