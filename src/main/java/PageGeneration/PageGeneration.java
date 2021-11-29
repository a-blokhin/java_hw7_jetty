package PageGeneration;

import DAO.ProductDAO;
import entity.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class PageGeneration {

    public String PageGet() throws SQLException {
        String result = "<html><head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>" +
                "<body>\n"+
                "<table><tr><th>ID</th><th>Product</th><th>Company</th><th>Quantity</th></tr>\n";

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db",
            "postgres", "123567");
        ProductDAO productDAO = new ProductDAO(connection);
        List<Product> rs = productDAO.all();
        for (Product product: rs) {
            result += ("<tr><td>" + product.getId() +
                    "</td><td>" + product.getName() +
                    "</td><td>" + product.getCompany() +
                    "</td><td>" + product.getQuantity() + "</td></tr>\n");
        }
        result+="</body></html>";
        return result;
    }

    public void PagePost(HttpServletRequest req) throws SQLException {
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
