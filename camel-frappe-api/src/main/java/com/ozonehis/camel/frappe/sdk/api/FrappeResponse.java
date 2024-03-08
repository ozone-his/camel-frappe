package com.ozonehis.camel.frappe.sdk.api;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.Closeable;
import java.io.InputStream;
import okhttp3.ResponseBody;

/**
 * This interface represents a Frappe response.
 */
public interface FrappeResponse extends Closeable {

    /**
     * This method returns the response as the specified type.
     *
     * @param returnType The type of the response.
     * @return The response as the specified type.
     */
    <T> T returnAs(Class<T> returnType);

    /**
     * This method returns the response as the specified type.
     *
     * @param typeReference The type of the response.
     * @return The response as the specified type.
     */
    <T> T returnAs(TypeReference<T> typeReference);

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

    /**
     * This method returns the response code.
     * @return The response code.
     */
    int code();

    /**
     * This method returns the response body.
     * @return The response body.
     */
    ResponseBody responseBody();
}
