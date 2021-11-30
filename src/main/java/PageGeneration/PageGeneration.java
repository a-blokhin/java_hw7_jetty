package PageGeneration;

import java.sql.SQLException;
import java.util.ArrayList;

public final class PageGeneration {

    public String PageGet(ArrayList<String> productList) throws SQLException {
        StringBuilder result = new StringBuilder("<html><head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>" +
                "<body>\n" +
                "<table><tr><th>ID</th><th>Product</th><th>Company</th><th>Quantity</th></tr>\n");
        productList.forEach(result::append);
        result.append("</body></html>");
        return result.toString();
    }

}
