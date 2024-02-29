package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.RemoteFrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.operation.ParameterizedOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractOperation<T> implements ParameterizedOperation<T> {
	
	protected final String url;
	
	protected final OkHttpClient httpClient;
	
	protected final TransformerFactory transformerFactory;
	
	protected final Map<String, List<String>> queryParams = new HashMap<>();
	
	protected final String baseApiUrl;
	
	protected final String[] pathParams;
	
	protected final String path;
	
	public AbstractOperation(String baseApiUrl, String path, OkHttpClient httpClient, TransformerFactory transformerFactory,
			String... pathParams) {
		this.baseApiUrl = baseApiUrl;
		this.path = path;
		this.url = (baseApiUrl.endsWith("/") ? baseApiUrl : baseApiUrl + "/") + (path != null && path.startsWith("/") ?
				path.substring(1) :
				path);
		this.httpClient = httpClient;
		this.transformerFactory = transformerFactory;
		this.pathParams = pathParams;
	}
	
	@Override
	public ParameterizedOperation<T> withParameters(Map<String, String> parameters) {
		parameters.forEach(this::withParameter);
		return this;
	}
	
	@Override
	public ParameterizedOperation<T> withParameter(String name, String value) {
		List<String> values = queryParams.computeIfAbsent(name, k -> new ArrayList<>());
		values.add(value);
		this.queryParams.put(name, values);
		return this;
	}
	
	@Override
	public T execute() {
		HttpUrl httpUrl = HttpUrl.parse(url);
		if (httpUrl == null || httpUrl.host().isEmpty()) {
			throw new IllegalArgumentException("Invalid URL: " + url);
		}
		
		HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
		queryParams.forEach((name, values) -> values.forEach(value -> httpUrlBuilder.addQueryParameter(name, value)));
		
		return doExecute(httpUrlBuilder.build());
	}
	
	protected abstract T doExecute(HttpUrl httpUrl);
	
	protected Optional<String> convertFiltersToString(List<List<String>> filters) {
		String filtersAsString = filters.stream()
				.map(filter -> filter.stream()
						.map(f -> "\"" + f + "\"")
						.collect(Collectors.joining(", ", "[", "]")))
				.collect(Collectors.joining(", ", "[", "]"));
		
		log.info("Filters: {}", filtersAsString);
		return Optional.of(filtersAsString);
	}
	
	protected Response onHttpResponse(Callable<Response> callable) {
		Response response;
		try {
			response = callable.call();
		} catch (Exception e) {
			throw new FrappeClientException(e);
		}
		
		if (!response.isSuccessful()) {
			String body = null;
			try {
				if (response.body() != null) {
					body = response.body().string();
					log.error("Error response: {}", body);
				}
			} catch (IOException e) {
				throw new FrappeClientException(e);
			}
			response.close();
			throw new RemoteFrappeClientException(response.toString(), response.code(), body);
		} else {
			return response;
		}
	}
}
