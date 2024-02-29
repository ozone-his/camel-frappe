package com.ozonehis.camel.frappe.sdk.api;

import java.io.Closeable;
import java.io.InputStream;

/**
 * This interface represents a Frappe response.
 */
public interface FrappeResponse extends Closeable {
	
	/**
	 * This method returns the response as the specified type.
	 *
	 * @param responseType The type of the response.
	 * @return The response as the specified type.
	 */
	<T> T returnAs( Class<T> responseType );
	
	
	/**
	 * This method returns the response as a stream.
	 * @return The response as a stream.
	 */
	InputStream read();
	
	/**
	 * This method returns the URL of the response.
	 * @return The URL of the response.
	 */
	String getUrl();
}
