package com.ozonehis.camel.frappe.sdk.internal.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozonehis.camel.frappe.sdk.api.transformer.RequestTransformer;
import com.ozonehis.camel.frappe.sdk.api.transformer.ResponseTransformer;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;

public class JacksonTransformerFactory implements TransformerFactory {
	
	private final ObjectMapper objectMapper;
	
	public JacksonTransformerFactory(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public JacksonTransformerFactory() {
		this(new ObjectMapper());
	}
	
	@Override
	public JacksonTransformer createTransformer() {
		return new JacksonTransformer(objectMapper);
	}
	
	@Override
	public <T> RequestTransformer<T> createRequestTransformer(Class<T> requestType) {
		return new JacksonRequestTransformer<>(requestType, createTransformer());
	}
	
	@Override
	public <T> ResponseTransformer<T> createResponseTransformer(Class<T> responseType) {
		return new JacksonResponseTransformer<>(responseType, createTransformer());
	}
}
