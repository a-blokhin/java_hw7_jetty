package servlet;

import DAO.ProductDAO;
import PageGeneration.PageGeneration;
import entity.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class GetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws   IOException {
        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html;charset=cp1251");
        PageGeneration pageGeneration = new PageGeneration();
        response.getWriter().println(pageGeneration.PageGet());
    }


}
