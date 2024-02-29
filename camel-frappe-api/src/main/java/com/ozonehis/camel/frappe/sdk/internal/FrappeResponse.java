package com.ozonehis.camel.frappe.sdk.internal;

import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import okhttp3.Response;

import java.io.InputStream;

public record FrappeResponse(Response response, TransformerFactory transformerFactory) implements com.ozonehis.camel.frappe.sdk.api.FrappeResponse {
	
	@Override
	public <T> T returnAs(Class<T> responseType) {
		return transformerFactory.createResponseTransformer(responseType).transform(response.body());
	}
	
	@Override
	public InputStream read() {
		if (response.body() != null) {
			return response.body().byteStream();
		}
		return null;
	}
	
	@Override
	public String getUrl() {
		return response.request().url().toString();
	}
	
	@Override
	public void close() {
		response.close();
	}
}
