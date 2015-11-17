package de.number26.configuration;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import de.number26.api.SumService;
import de.number26.services.SumServiceImpl;
import de.number26.storage.TransactionStorage;
import de.number26.storage.impl.HashMapBasedTransactionStorage;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vasiliy
 */
public class MainModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // servlets, later to be replaced with some configuration
        bind(DefaultServlet.class).in(Singleton.class);
        bind(TransactionStorage.class).to(HashMapBasedTransactionStorage.class).in(Singleton.class);
        bind(SumService.class).to(SumServiceImpl.class);

        // jackson
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        Map<String, String> options = new HashMap<>();
        options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        serve("/*").with(GuiceContainer.class, options);
    }
}