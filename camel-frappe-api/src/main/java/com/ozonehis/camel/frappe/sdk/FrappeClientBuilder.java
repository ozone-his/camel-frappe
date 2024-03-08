package com.ozonehis.camel.frappe.sdk;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.security.FrappeBasicAuthentication;
import com.ozonehis.camel.frappe.sdk.internal.transformer.JacksonTransformer;

import java.util.concurrent.TimeUnit;

public class FrappeClientBuilder {
	
	private final FrappeAuthentication authentication;
	
	private final String baseApiUrl;
	
	private int maxIdleConnections = 5;
	
	private long keepAliveDurationMs = 300000;
	
	private long callTimeoutMs = 0;
	
	private long readTimeoutMs = 10000;
	
	private long writeTimeoutMs = 10000;
	
	private long connectTimeoutMs = 10000;
	
	// Set default transformer factory to JacksonTransformerFactory
	private Transformer transformer = new JacksonTransformer();
	
	private FrappeClientBuilder(String baseApiUrl, FrappeAuthentication authentication) {
		this.baseApiUrl = baseApiUrl != null ? baseApiUrl.trim() : null;
		this.authentication = authentication;
	}
	
	public static FrappeClientBuilder newClient(String baseApiUrl, FrappeAuthentication authentication) {
		return new FrappeClientBuilder(baseApiUrl, authentication);
	}
	
	public static FrappeClientBuilder newClient(String baseApiUrl, String username, String password) {
		return new FrappeClientBuilder(baseApiUrl, new FrappeBasicAuthentication(username, password));
	}
	
	public FrappeClientBuilder maxIdleConnections(int maxIdleConnections) {
		this.maxIdleConnections = maxIdleConnections;
		return this;
	}
	
	public FrappeClientBuilder keepAliveDurationMs(long keepAliveDurationMs, TimeUnit timeUnit) {
		this.keepAliveDurationMs = timeUnit.toMillis(keepAliveDurationMs);
		return this;
	}
	
	public FrappeClientBuilder callTimeoutMs(long callTimeoutMs, TimeUnit timeUnit) {
		this.callTimeoutMs = timeUnit.toMillis(callTimeoutMs);
		return this;
	}
	
	public FrappeClientBuilder readTimeoutMs(long readTimeoutMs, TimeUnit timeUnit) {
		this.readTimeoutMs = timeUnit.toMillis(readTimeoutMs);
		return this;
	}
	
	public FrappeClientBuilder writeTimeoutMs(long writeTimeoutMs, TimeUnit timeUnit) {
		this.writeTimeoutMs = timeUnit.toMillis(writeTimeoutMs);
		return this;
	}
	
	public FrappeClientBuilder connectTimeoutMs(long connectTimeoutMs, TimeUnit timeUnit) {
		this.connectTimeoutMs = timeUnit.toMillis(connectTimeoutMs);
		return this;
	}
	
	public FrappeClientBuilder withTransformer(Transformer transformer) {
		this.transformer = transformer;
		return this;
	}
	
	public DefaultFrappeClient build() {
		StringBuilder baseApiPath = new StringBuilder();
		baseApiPath.append(this.baseApiUrl);
		if ( !baseApiUrl.endsWith( "/" ) ) {
			baseApiPath.append( "/" );
		}
		
		return new DefaultFrappeClient(baseApiPath.toString(), authentication, transformer,
				maxIdleConnections, keepAliveDurationMs, callTimeoutMs, readTimeoutMs, writeTimeoutMs, connectTimeoutMs);
	}
}
