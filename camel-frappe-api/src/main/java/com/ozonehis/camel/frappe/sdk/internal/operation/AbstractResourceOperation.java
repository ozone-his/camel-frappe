package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Abstract class for resource operations. It provides a resource field and a doResourceExecute method.
 */
@Slf4j
public abstract class AbstractResourceOperation extends AbstractOperation<FrappeResponse> implements ResourceOperation {
	
	protected Object resource;
	
	public AbstractResourceOperation(String baseApiUrl, String path, OkHttpClient httpClient,
			TransformerFactory transformerFactory, String... pathParams) {
		super(baseApiUrl, path, httpClient, transformerFactory, pathParams);
	}
	
	
	@Override
	public FrappeResponse doExecute(HttpUrl httpUrl) {
		HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
		Request.Builder requestBuilder = new Request.Builder().url(httpUrlBuilder.build())
				.addHeader("Content-Type", "application/json");
		final byte[] bytes = transformResourceToBytes(resource);
		return doResourceExecute(bytes, requestBuilder);
	}
	
	@SuppressWarnings("unchecked")
	protected byte[] transformResourceToBytes(Object resource) {
		return Optional.ofNullable(resource)
				.map(res -> {
					if (res instanceof String) {
						return ((String) res).getBytes(StandardCharsets.UTF_8);
					} else {
						return transformerFactory.createRequestTransformer((Class) res.getClass()).transform(res)
								.getBytes(StandardCharsets.UTF_8);
					}
				})
				.orElse(new byte[]{});
	}
	
	protected abstract FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder);
	
	@Override
	public ResourceOperation withResource(Object resource) {
		this.resource = resource;
		return this;
	}
}
