package ning.codelab.finance;

import ning.codelab.finance.servlet.FinanceServletListener;

import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.webapp.WebAppContext;

public class AppLauncher
{

    public static void main(String[] args) throws Exception
    {
        // Create the server.
        Server server = new Server(8080);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase("src/main/webapp/");
        webAppContext.setParentLoaderPriority(true);

        webAppContext.addEventListener(new FinanceServletListener());
        webAppContext.addFilter(GuiceFilter.class, "/*", null);


        webAppContext.addServlet(DefaultServlet.class, "/");

        server.setHandler(webAppContext);

        /*
         * // Create a servlet context and add the jersey servlet. ServletContextHandler sch = new ServletContextHandler(server,
         * "/");
         * 
         * // Add our Guice listener that includes our bindings sch.addEventListener(new FinanceServletListener());
         * sch.setResourceBase("src/main/webapp/"); // Then add GuiceFilter and configure the server to // reroute all requests
         * through this filter. sch.addFilter(GuiceFilter.class, "/*", null);
         * 
         * // Must add DefaultServlet for embedded Jetty. // Failing to do this will cause 404 errors. // This is not needed if
         * web.xml is used instead. sch.addServlet(DefaultServlet.class, "/");
         */
        // Start the server
        server.start();
        server.join();
    }

}
