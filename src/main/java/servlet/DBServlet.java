package servlet;

import PageGeneration.PageGeneration;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public final class DBServlet extends HttpServlet {
    final Injector injector = Guice.createInjector();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws   IOException {
        response.setCharacterEncoding("cp1251");
        response.setContentType("text/html;charset=cp1251");
        response.getWriter().println(injector.getInstance(PageGeneration.class).PageGet());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("cp1251");
        resp.setContentType("text/html;charset=cp1251");
        try{
            injector.getInstance(PageGeneration.class).PagePost(req);
            resp.setStatus(HttpServletResponse.SC_OK);
        }catch (NullPointerException nullException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters");
        } catch (SQLException throwables) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLException");
        }
    }
}