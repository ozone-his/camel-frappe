package com.ozonehis.camel;

import com.ozonehis.camel.api.proxy.FrappeDelete;
import com.ozonehis.camel.api.proxy.FrappeGet;
import com.ozonehis.camel.api.proxy.FrappePost;
import com.ozonehis.camel.api.proxy.FrappePut;
import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappeApiName;
import com.ozonehis.camel.internal.FrappeConstants;
import com.ozonehis.camel.internal.FrappePropertiesHelper;
import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.support.component.AbstractApiEndpoint;
import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import java.util.Map;

/**
 *  Frappe component to integrate with Frappe REST API.
 *
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "frappe", title = "Frappe", syntax="frappe:apiName/methodName",
            apiSyntax = "apiName/methodName",category = {Category.API})
public class FrappeEndpoint extends AbstractApiEndpoint<FrappeApiName, FrappeConfiguration> {
    
    @UriParam
    private final FrappeConfiguration configuration;
    
    private Object apiProxy;

    public FrappeEndpoint(String uri, FrappeComponent component,
                         FrappeApiName apiName, String methodName, FrappeConfiguration endpointConfiguration) {
        super(uri, component, apiName, methodName, FrappeApiCollection.getCollection().getHelper(apiName), endpointConfiguration);
	    this.configuration = endpointConfiguration;
    }

    public Producer createProducer() throws Exception {
        return new FrappeProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        // make sure inBody is not set for consumers
        if (inBody != null) {
            throw new IllegalArgumentException("Option inBody is not supported for consumer endpoint");
        }
        final FrappeConsumer consumer = new FrappeConsumer(this, processor);
        // also set consumer.* properties
        configureConsumer(consumer);
        return consumer;
    }

    @Override
    protected ApiMethodPropertiesHelper<FrappeConfiguration> getPropertiesHelper() {
        return FrappePropertiesHelper.getHelper(getCamelContext());
    }

    protected String getThreadProfileName() {
        return FrappeConstants.THREAD_PROFILE_NAME;
    }

    @Override
    protected void afterConfigureProperties() {
        switch (apiName) {
            case GET:
                apiProxy = new FrappeGet(getClient());
                break;
            case POST:
                apiProxy = new FrappePost(getClient());
                break;
            case PUT:
                apiProxy = new FrappePut(getClient());
                break;
            case DELETE:
                apiProxy = new FrappeDelete(getClient());
                break;
            default:
                throw new IllegalArgumentException("Invalid API name " + apiName);
        }
    }

    @Override
    public Object getApiProxy(ApiMethod method, Map<String, Object> args) {
        return apiProxy;
    }
    
    protected FrappeClient getClient() {
        return ((FrappeComponent) this.getComponent()).getClient(this.configuration);
    }
}
