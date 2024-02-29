package com.ozonehis.camel.frappe.sdk.api.transformer;

/**
 * This interface represents a request transformer.
 *
 * @param <T> The type of the request body.
 */
public interface RequestTransformer<T> {
	
	/**
	 * This method transforms the request body.
	 *
	 * @param requestBody The request body.
	 * @return The transformed request body.
	 */
	String transform( T requestBody );
}
