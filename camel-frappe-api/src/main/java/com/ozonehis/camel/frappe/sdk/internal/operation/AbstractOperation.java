package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.operation.ParameterizedOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

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
	
	protected final Transformer transformer;
	
	protected final Map<String, List<String>> queryParams = new HashMap<>();
	
	protected final String baseApiUrl;
	
	protected final String[] pathParams;
	
	protected final String doctype;
	
	public AbstractOperation(String baseApiUrl, String doctype, OkHttpClient httpClient,
			Transformer transformer,
			String... pathParams) {
		this.baseApiUrl = baseApiUrl;
		this.doctype = doctype;
		this.url = buildUrl(baseApiUrl) + (doctype != null && doctype.startsWith("/") ?
				doctype.substring(1) : doctype);
		this.httpClient = httpClient;
		this.transformer = transformer;
		this.pathParams = pathParams;
	}
	
	private String buildUrl(String baseApiUrl) {
		StringBuilder urlBuilder = new StringBuilder(baseApiUrl);
		if (!baseApiUrl.endsWith("/")) {
			urlBuilder.append("/");
		}
		// TODO: Add proper support for resource and method paths. Now just support resource paths
		if (!baseApiUrl.endsWith("resource/")) {
			urlBuilder.append("resource/");
		}
		return urlBuilder.toString();
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
		
		// Add path parameters
		if (pathParams != null) {
			for (String pathParam : pathParams) {
				httpUrl = httpUrl.newBuilder().addPathSegment(pathParam).build();
			}
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
		
		return Optional.of(filtersAsString);
	}
	
	protected Response onHttpResponse(Callable<Response> callable) {
		try {
			return  callable.call();
		} catch (Exception e) {
			throw new FrappeClientException(e);
		}
	}
}
