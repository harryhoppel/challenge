package de.number26;

import com.google.inject.Guice;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import de.number26.configuration.CommandLineConfig;
import de.number26.configuration.MainModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class Challenge implements Application {
    private static final Logger LOG = LoggerFactory.getLogger(Challenge.class);

    private Server server;

    public Challenge() {}

    public static void main(String[] args) {
        try {
            new Challenge().start(new CommandLineConfig(args));
        } catch (Throwable throwable) {
            // cleanup? etc.
            LOG.error("Exiting with throwable", throwable);
        }
    }

    public void start(CommandLineConfig commandLineConfig) throws Exception {
        Guice.createInjector(Stage.PRODUCTION, new MainModule());
        server = new Server(commandLineConfig.getPort());
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addFilter(GuiceFilter.class, "/*", EnumSet.of(javax.servlet.DispatcherType.REQUEST, javax.servlet.DispatcherType.ASYNC));
        context.addServlet(DefaultServlet.class, "/*");
        server.start();
    }

    @Override
    public void stop() {
        if (server.isStarted()) {
            try {
                server.stop();
            } catch (Exception e) {
                server.destroy();
            }
        }
    }
}
