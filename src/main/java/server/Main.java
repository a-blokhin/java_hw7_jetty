package server;

import filter.GetFilter;
import filter.PostFilter;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.flywaydb.core.Flyway;
import servlet.GetServlet;
import servlet.PostServlet;

import javax.servlet.DispatcherType;
import java.net.URL;
import java.util.EnumSet;

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

        final PostFilter postfilter = new PostFilter();
        final FilterHolder postholder = new FilterHolder(postfilter);
        final GetFilter getfilter = new GetFilter();
        final FilterHolder getholder = new FilterHolder(getfilter);
        context.addFilter(postholder,"/postProduct", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(getholder,"/getProducts", EnumSet.of(DispatcherType.REQUEST));

        final String hashConfig = Main.class.getResource("/hash_config").toExternalForm();
        final HashLoginService hashLoginService = new HashLoginService("login",hashConfig);
        final ConstraintSecurityHandler security = new SecurityHandlerBuilder().build(hashLoginService);

        server.addBean(hashLoginService);
        security.setHandler(context);
        server.setHandler(security);

        server.start();
    }
}
