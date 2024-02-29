package com.ozonehis.camel.frappe.sdk.internal.transformer;

import com.ozonehis.camel.frappe.sdk.api.transformer.RequestTransformer;

public class JacksonRequestTransformer<W> implements RequestTransformer<W> {
	
	private final Class<W> requestType;
	
	private final JacksonTransformer jacksonTransformer;
	
	public JacksonRequestTransformer(Class<W> requestType, JacksonTransformer jacksonTransformer) {
		this.requestType = requestType;
		this.jacksonTransformer = jacksonTransformer;
	}
	
	@Override
	public String transform(W requestBody) {
		return jacksonTransformer.transform(requestBody);
	}
}
