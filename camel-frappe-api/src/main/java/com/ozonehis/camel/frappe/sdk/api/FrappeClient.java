package com.ozonehis.camel.frappe.sdk.api;

import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import okhttp3.OkHttpClient;

/**
 * This interface represents a Frappe client.
 */
public interface FrappeClient {
	
	/**
	 * This method creates a GET operation.
	 *
	 * @param doctype The doctype.
	 * @param pathParams The doctype parameters.
	 * @return The GET operation.
	 */
	GetOperation get(String doctype, String... pathParams);
	
	/**
	 * This method creates a POST operation.
	 *
	 * @param doctype The doctype.
	 * @param pathParams The doctype parameters.
	 * @return The POST operation.
	 */
	ResourceOperation post(String doctype, String... pathParams);
	
	/**
	 * This method creates a PUT operation.
	 *
	 * @param doctype The doctype.
	 * @param pathParams The doctype parameters.
	 * @return The PUT operation.
	 */
	ResourceOperation put(String doctype, String... pathParams);
	
	/**
	 * This method gets the HTTP client.
	 *
	 * @return The HTTP client.
	 */
	OkHttpClient getHttpClient();
	
	/**
	 * This method gets the base API URL.
	 *
	 * @return The base API URL.
	 */
	String getBaseApiUrl();
	
	/**
	 * This method gets the transformer factory.
	 *
	 * @return The transformer factory.
	 */
	TransformerFactory getTransformerFactory();
}
