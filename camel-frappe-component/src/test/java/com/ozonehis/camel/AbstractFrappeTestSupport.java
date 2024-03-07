package com.ozonehis.camel;

import com.ozonehis.camel.frappe.sdk.FrappeClientBuilder;
import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Map;

/**
 * Abstract base class for Frappé Integration tests.
 */
@Slf4j
@Testcontainers
public abstract class AbstractFrappeTestSupport extends CamelTestSupport {
    
    @Container
    public static ComposeContainer environment = new ComposeContainer(new File("src/test/resources/docker/compose-test.yml"))
                    .withLocalCompose(true)
                    .withStartupTimeout(java.time.Duration.ofMinutes(5))
                    .withExposedService("frontend", 8080, Wait.forHttp("/").forStatusCode(200))
                    .withTailChildContainers(true);
    
    public static FrappeClient getFrappeClient() {
        var host = environment.getServiceHost("frontend", 8080);
        var port = environment.getServicePort("frontend", 8080);
        var apiUrl = "http://" + host + ":" + port + "/api/resource";
        
        return FrappeClientBuilder.newClient(apiUrl, "Administrator", "admin").build();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        final FrappeConfiguration configuration = new FrappeConfiguration();
        configuration.setFrappeClient(getFrappeClient());
        // add FrappeComponent to Camel context
        final FrappeComponent component = new FrappeComponent(context);
        component.setConfiguration(configuration);
        context.addComponent("frappe", component);
        log.info("FrappeComponent added to Camel context");
        return context;
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers) throws CamelExecutionException {
        return (T) this.template().requestBodyAndHeaders(endpointUri, body, headers);
    }

    @SuppressWarnings("unchecked")
    protected <T> T requestBody(String endpoint, Object body) throws CamelExecutionException {
        return (T) this.template().requestBody(endpoint, body);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T requestBody(String endpoint, Object body, Class<T> type) throws CamelExecutionException {
        return this.template().requestBody(endpoint, body, type);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers, Class<T> type) throws CamelExecutionException {
        return this.template().requestBodyAndHeaders(endpointUri, body, headers, type);
    }
}
