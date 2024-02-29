package com.ozonehis.camel;

import com.ozonehis.camel.frappe.sdk.FrappeClientBuilder;
import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappeApiName;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Metadata;
import org.apache.camel.support.component.AbstractApiComponent;

@org.apache.camel.spi.annotations.Component("frappe")
public class FrappeComponent extends AbstractApiComponent<FrappeApiName, FrappeConfiguration, FrappeApiCollection> {
	
	@Metadata(label = "advanced")
	FrappeConfiguration configuration;
	
	private FrappeClient frappeClient;
	
	public FrappeComponent() {
		super(FrappeApiName.class, FrappeApiCollection.getCollection());
	}
	
	public FrappeComponent(CamelContext context) {
		super(context, FrappeApiName.class, FrappeApiCollection.getCollection());
	}
	
	@Override
	protected FrappeApiName getApiName(String apiNameStr) throws IllegalArgumentException {
		return getCamelContext().getTypeConverter().convertTo(FrappeApiName.class, apiNameStr);
	}
	
	@Override
	protected Endpoint createEndpoint(String uri, String methodName, FrappeApiName apiName,
			FrappeConfiguration endpointConfiguration) {
		endpointConfiguration.setApiName(apiName);
		endpointConfiguration.setMethodName(methodName);
		return new FrappeEndpoint(uri, this, apiName, methodName, endpointConfiguration);
	}
	
	/**
	 * To use the shared configuration
	 */
	@Override
	public void setConfiguration(FrappeConfiguration configuration) {
		super.setConfiguration(configuration);
	}
	
	public FrappeClient getClient(FrappeConfiguration endpointConfiguration) {
		if (endpointConfiguration.equals(this.configuration)) {
			synchronized (this) {
				if (this.frappeClient == null) {
					this.frappeClient = FrappeClientBuilder.newClient(endpointConfiguration.getBaseApiUrl(),
							endpointConfiguration.getUsername(), endpointConfiguration.getPassword()).build();
				}
			}
			return this.frappeClient;
		} else {
			if (endpointConfiguration.getClient() != null) {
				if (endpointConfiguration.getBaseApiUrl() != null || endpointConfiguration.getUsername() != null
						|| endpointConfiguration.getPassword() != null) {
					throw new RuntimeCamelException(
							"Bad Frappe endpoint configuration: client option is mutually exclusive to baseApiUrl, username, password. Either set `client`, or `baseApiUrl` and `username` and `password`");
				}
				
				return endpointConfiguration.getClient();
			} else {
				return FrappeClientBuilder.newClient(endpointConfiguration.getBaseApiUrl(),
						endpointConfiguration.getUsername(), endpointConfiguration.getPassword()).build();
			}
		}
	}
}
