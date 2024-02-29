package com.ozonehis.camel.frappe.sdk.internal.transformer;

import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.transformer.ResponseTransformer;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonResponseTransformer<W> implements ResponseTransformer<W> {
	
	private final Class<W> returnType;
	
	private final JacksonTransformer jacksonTransformer;
	
	public JacksonResponseTransformer(Class<W> returnType, JacksonTransformer jacksonTransformer) {
		this.returnType = returnType;
		this.jacksonTransformer = jacksonTransformer;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public W transform(ResponseBody responseBody) {
		try (responseBody) {
			if (returnType.equals(String.class)) {
				return (W) responseBody.string();
			} else {
				return jacksonTransformer.transform(responseBody.charStream(), returnType);
			}
		}
		catch (IOException e) {
			throw new FrappeClientException(e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public W transform(List<Map<String, Object>> responseBody) {
		return (W) jacksonTransformer.transform(responseBody, List.class, returnType);
	}
}
