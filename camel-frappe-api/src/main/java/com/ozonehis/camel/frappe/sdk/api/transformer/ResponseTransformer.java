package com.ozonehis.camel.frappe.sdk.api.transformer;

import okhttp3.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * This interface represents a response transformer.
 *
 * @param <T> The type of the response body.
 */
public interface ResponseTransformer<T> {
	
	/**
	 * This method transforms the response body.
	 *
	 * @param responseBody The response body.
	 * @return The transformed response body.
	 */
	T transform(ResponseBody responseBody);
	
	/**
	 * This method transforms the response body.
	 *
	 * @param responseBody The response body.
	 * @return The transformed response body.
	 */
	T transform(List<Map<String, Object>> responseBody);
}
