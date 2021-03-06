package server;

import commons.Connect;
import commons.DefaultServer;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import servlet.DBServlet;

import java.net.URL;

public final class Main {
    public static void main(String[] args) throws Exception{
        Connect.migrate();

        Server server = new DefaultServer().build();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        final URL resource = LoginService.class.getResource("/static");
        context.setBaseResource(Resource.newResource(resource.toExternalForm()));
        context.setWelcomeFiles(new String[]{"/static/example"});

        context.addServlet(new ServletHolder("default", DefaultServlet.class),"/");
        context.addServlet(new ServletHolder("postProduct", DBServlet.class),"/postProduct");
        context.addServlet(new ServletHolder("getProducts", DBServlet.class),"/getProducts");

        final String hashConfig = Main.class.getResource("/hash_config").toExternalForm();
        final HashLoginService hashLoginService = new HashLoginService("login",hashConfig);
        final ConstraintSecurityHandler security = new SecurityHandlerBuilder().build(hashLoginService);

        server.addBean(hashLoginService);
        security.setHandler(context);
        server.setHandler(security);

        server.start();
    }
}
