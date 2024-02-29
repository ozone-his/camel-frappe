package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultGetOperation extends AbstractOperation<FrappeResponse> implements GetOperation {
	
	protected final List<String> fields = new ArrayList<>();
	
	protected List<List<String>> filters = new ArrayList<>();
	
	public DefaultGetOperation(String baseApiUrl, String path, OkHttpClient httpClient,
			TransformerFactory transformerFactory, String... pathParams) {
		super(baseApiUrl, path, httpClient, transformerFactory, pathParams);
	}
	
	@Override
	protected FrappeResponse doExecute(HttpUrl httpUrl) {
		HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
		if (!fields.isEmpty()) {
			StringBuilder fieldsAsString = new StringBuilder();
			fieldsAsString.append("[");
			for (int i = 0; i < fields.size(); i++) {
				fieldsAsString.append("\"").append(fields.get(i)).append("\"");
				if (i < fields.size() - 1) {
					fieldsAsString.append(", ");
				}
			}
			fieldsAsString.append("]");
			httpUrlBuilder.addQueryParameter("fields", fieldsAsString.toString());
		}
		
		this.convertFiltersToString(filters)
				.ifPresent(filtersAsString -> httpUrlBuilder.addQueryParameter("filters", filtersAsString));
		
		okhttp3.Response response = onHttpResponse(
				() -> httpClient.newCall(new Request.Builder().url(httpUrlBuilder.build()).get().build()).execute());
		
		return new DefaultFrappeResponse(response, transformerFactory);
	}
	
	@Override
	public GetOperation withFields(List<String> fields) {
		this.fields.addAll(fields);
		return this;
	}
	
	@Override
	public GetOperation withField(String field) {
		this.fields.add(field);
		return this;
	}
	
	@Override
	public GetOperation withFilters(List<List<String>> filters) {
		this.filters.addAll(filters);
		return this;
	}
	
	@Override
	public GetOperation withFilter(List<String> filter) {
		this.filters.add(filter);
		return this;
	}
}
