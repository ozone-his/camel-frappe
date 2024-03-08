package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.DeleteOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Getter
public class DefaultDeleteOperation extends AbstractResourceOperation implements DeleteOperation {

    private String nameOfResourceToBeDeleted;

    public DefaultDeleteOperation(String baseApiUrl, String path, OkHttpClient httpClient, Transformer transformer) {
        super(baseApiUrl, path, httpClient, transformer);
    }

    @Override
    protected FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder) {
        Request request = requestBuilder
                .delete()
                .url(baseApiUrl + doctype + "/" + nameOfResourceToBeDeleted)
                .build();
        okhttp3.Response response =
                onHttpResponse(() -> httpClient.newCall(request).execute());
        return new DefaultFrappeResponse(response, transformer);
    }

    @Override
    public DeleteOperation withName(String name) {
        this.nameOfResourceToBeDeleted = name;
        return this;
    }
}
