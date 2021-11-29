package servlet;

import DAO.ProductDAO;
import PageGeneration.PageGeneration;
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
            PageGeneration pageGeneration = new PageGeneration();
            pageGeneration.PagePost(req, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        }catch (NullPointerException nullException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters");
        } catch (SQLException throwables) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
}
