package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.PostOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Slf4j
public class DefaultPostOperation extends AbstractResourceOperation implements PostOperation {

    public DefaultPostOperation(
            String baseApiUrl, String doctype, OkHttpClient httpClient, Transformer transformer, String... pathParams) {
        super(baseApiUrl, doctype, httpClient, transformer, pathParams);
    }

    @Override
    protected FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder) {
        Request request =
                requestBuilder.post(RequestBody.create(resourceAsBytes)).build();
        okhttp3.Response response =
                onHttpResponse(() -> httpClient.newCall(request).execute());
        return new DefaultFrappeResponse(response, transformer);
    }
}
