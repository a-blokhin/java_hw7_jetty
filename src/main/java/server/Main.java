package server;

import commons.DefaultServer;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.flywaydb.core.Flyway;
import servlet.GetServlet;
import servlet.PostServlet;

import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception{
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/db","postgres","123567")
                .locations("db")
                .load();
        flyway.migrate();

        Server server = new DefaultServer().build();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        final URL resource = LoginService.class.getResource("/static");
        context.setBaseResource(Resource.newResource(resource.toExternalForm()));
        context.setWelcomeFiles(new String[]{"/static/example"});

        context.addServlet(new ServletHolder("default", DefaultServlet.class),"/");
        context.addServlet(new ServletHolder("postProduct", PostServlet.class),"/postProduct");
        context.addServlet(new ServletHolder("getProducts", GetServlet.class),"/getProducts");

        final String hashConfig = Main.class.getResource("/hash_config").toExternalForm();
        final HashLoginService hashLoginService = new HashLoginService("login",hashConfig);
        final ConstraintSecurityHandler security = new SecurityHandlerBuilder().build(hashLoginService);

        server.addBean(hashLoginService);
        security.setHandler(context);
        server.setHandler(security);

        server.start();
    }
}
