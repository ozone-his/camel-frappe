package com.ozonehis.camel.frappe.sdk.api.transformer;

/**
 * This interface represents a transformer factory.
 */
public interface TransformerFactory {
	
	/**
	 * This method creates a transformer.
	 *
	 * @return The transformer.
	 */
	Transformer createTransformer();
	
	/**
	 * This method creates a request transformer.
	 *
	 * @param requestType The type of the request body.
	 * @return The request transformer.
	 */
	<T> RequestTransformer<T> createRequestTransformer(Class<T> requestType);
	
	/**
	 * This method creates a response transformer.
	 *
	 * @param responseType The type of the response body.
	 * @return The response transformer.
	 */
	<T> ResponseTransformer<T> createResponseTransformer(Class<T> responseType);
}
