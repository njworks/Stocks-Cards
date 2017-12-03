package web;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatServer implements Runnable {

    public static final int TOMCAT_PORT = 8080;
    public static final String EXAMPLES_URL = "http://localhost:8080/examples/rest";
    public static final String BOT_URL = EXAMPLES_URL + "/bot";

    public TomcatServer() {
    }

    //Runs the Tomcat on a thread
    @Override
    public synchronized void run() {
        try {
            start();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void start() throws ServletException, LifecycleException {
        // JAX-RS (Jersey) configuration
        ResourceConfig config = new ResourceConfig();
        // Packages where Jersey looks for web service classes
        // Do not forget to add webserver package with Gson provider
        config.packages("bot", "solutions", "webserver");
        // Enable logging for debugging purposes
        // Comment out next line to turn off logging
        config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, "INFO");
        // Tomcat configuration
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(TOMCAT_PORT);
        // Add web application
        Context context = tomcat.addWebapp("/examples", new File("./WebContent").getAbsolutePath());
        // Declare Jersey as a servlet
        Tomcat.addServlet(context, "jersey", new ServletContainer(config));
        // Map certain URLs to Jersey
        context.addServletMappingDecoded("/rest/*", "jersey");
        // Start server
        tomcat.start();
        tomcat.getServer().await();
    }

}
