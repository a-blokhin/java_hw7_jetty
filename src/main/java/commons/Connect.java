package commons;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "jdbc:postgresql://localhost:5432/db";
    private static final String username = "postgres";
    private static final String password = "123567";
    private static final String location = "db";

    public static void migrate(){
        Flyway flyway = Flyway.configure()
                .dataSource(URL,username,password)
                .locations(location)
                .load();
        flyway.migrate();
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL,username,password);
    }
}
