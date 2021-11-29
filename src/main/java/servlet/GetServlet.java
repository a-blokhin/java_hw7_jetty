package servlet;

import DAO.ProductDAO;
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
        response.getWriter().println(PageGet());
    }

    public String PageGet(){
        String result = "<html><head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>" +
                "<body>\n"+
                "<table><tr><th>ID</th><th>Product</th><th>Company</th><th>Quantity</th></tr>\n";
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db",
                    "postgres", "123567");
            ProductDAO productDAO = new ProductDAO(connection);
            List<Product> rs = productDAO.all();
            for (Product product: rs) {
                result+=("<tr><td>" + product.getId() +
                        "</td><td>" + product.getName() +
                        "</td><td>" + product.getCompany() +
                        "</td><td>" + product.getQuantity() + "</td></tr>\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        result+="</body></html>";
        return result;
    }
}
