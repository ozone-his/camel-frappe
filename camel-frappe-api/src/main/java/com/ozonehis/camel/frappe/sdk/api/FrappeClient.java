package com.ozonehis.camel.frappe.sdk.api;

import com.ozonehis.camel.frappe.sdk.api.operation.DeleteOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.PostOperation;
import com.ozonehis.camel.frappe.sdk.api.operation.PutOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
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
     * @return The POST operation.
     */
    PostOperation post(String doctype);

    /**
     * This method creates a POST operation.
     *
     * @param doctype The doctype.
     * @param resource The resource.
     * @param <R> The resource type.
     * @return The POST operation.
     */
    <R> PostOperation post(String doctype, R resource);

    /**
     * This method creates a PUT operation.
     *
     * @param doctype The doctype.
     * @return The PUT operation.
     */
    PutOperation put(String doctype);

    /**
     * This method creates a DELETE operation.
     *
     * @param doctype The doctype.
     * @return The DELETE operation.
     */
    DeleteOperation delete(String doctype);

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
     * This method gets the transformer.
     *
     * @return The transformer.
     */
    Transformer getTransformer();

    /**
     * This method sets the base URL.
     *
     * @param url The base URL.
     */
    void setBaseUrl(String url);
}
