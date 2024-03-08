package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.PutOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DefaultPutOperation extends AbstractResourceOperation implements PutOperation {

    private String nameOfResourceToBeUpdated;

    public DefaultPutOperation(
            String baseApiUrl, String path, OkHttpClient httpClient, Transformer transformer, String... pathParams) {
        super(baseApiUrl, path, httpClient, transformer, pathParams);
    }

    @Override
    protected FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder) {
        Request request = requestBuilder
                .put(RequestBody.create(resourceAsBytes))
                .url(baseApiUrl + doctype + "/" + nameOfResourceToBeUpdated)
                .build();
        okhttp3.Response response =
                onHttpResponse(() -> httpClient.newCall(request).execute());
        return new DefaultFrappeResponse(response, transformer);
    }

    @Override
    public void withName(String name) {
        this.nameOfResourceToBeUpdated = name;
    }
}
