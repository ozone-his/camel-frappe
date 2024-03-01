package com.ozonehis.camel.frappe.sdk;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;
import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultGetOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultPostOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultPutOperation;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.InMemoryCookieJar;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class DefaultFrappeClient implements FrappeClient {
	
	private final OkHttpClient httpClient;
	
	private final String baseApiUrl;
	
	private final TransformerFactory transformerFactory;
	
	public DefaultFrappeClient(String baseApiUrl, FrappeAuthentication authentication, TransformerFactory transformerFactory,
			int maxIdleConnections, long keepAliveDuration, long callTimeout, long readTimeout, long writeTimeout,
			long connectTimeout) {
		InMemoryCookieJar inMemoryCookieJar = new InMemoryCookieJar();
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
				.addInterceptor(authentication)
				.cookieJar(inMemoryCookieJar)
				.connectionPool( new ConnectionPool( maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS ) )
				.callTimeout( callTimeout, TimeUnit.MILLISECONDS ).readTimeout( readTimeout, TimeUnit.MILLISECONDS )
				.writeTimeout( writeTimeout, TimeUnit.MILLISECONDS )
				.connectTimeout( connectTimeout, TimeUnit.MILLISECONDS);
		
		this.httpClient = httpClientBuilder.build();
		this.baseApiUrl = baseApiUrl;
		this.transformerFactory = transformerFactory;
	}
	
	@Override
	public GetOperation get(String doctype, String... pathParams) {
		return new DefaultGetOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformerFactory(), pathParams);
	}
	
	@Override
	public ResourceOperation post(String doctype, String... pathParams) {
		return new DefaultPostOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformerFactory(), pathParams);
	}
	
	@Override
	public ResourceOperation put(String doctype, String... pathParams) {
		return new DefaultPutOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformerFactory(), pathParams);
	}
	
	@Override
	public OkHttpClient getHttpClient() {
		return this.httpClient;
	}
	
	@Override
	public String getBaseApiUrl() {
		return this.baseApiUrl;
	}
	
	@Override
	public TransformerFactory getTransformerFactory() {
		return this.transformerFactory;
	}
}
