package com.ozonehis.camel.frappe.sdk;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.DeleteOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.PostOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.PutOperation;
import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultDeleteOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultGetOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultPostOperation;
import com.ozonehis.camel.frappe.sdk.internal.operation.DefaultPutOperation;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.InMemoryCookieJar;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class DefaultFrappeClient implements FrappeClient {
	
	private final OkHttpClient httpClient;
	
	private String baseApiUrl;
	
	private final Transformer transformer;
	
	public DefaultFrappeClient(String baseApiUrl, FrappeAuthentication authentication, Transformer transformer,
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
		this.transformer = transformer;
	}
	
	@Override
	public GetOperation get(String doctype, String... pathParams) {
		return new DefaultGetOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformer(), pathParams);
	}
	
	@Override
	public PostOperation post(String doctype) {
		return new DefaultPostOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformer());
	}
	
	@Override
	public <R> PostOperation post(String doctype, R resource) {
		PostOperation postOperation = new DefaultPostOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformer());
		postOperation.withResource(resource);
		return postOperation;
	}
	
	@Override
	public PutOperation put(String doctype) {
		return new DefaultPutOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformer());
	}
	
	@Override
	public DeleteOperation delete(String doctype) {
		return new DefaultDeleteOperation(getBaseApiUrl(), doctype, getHttpClient(), getTransformer());
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
	public Transformer getTransformer() {
		return this.transformer;
	}
	
	@Override
	public void setBaseUrl(String url) {
		this.baseApiUrl = url;
	}
}
