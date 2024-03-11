package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Abstract class for resource operations.
 */
@Slf4j
public abstract class AbstractResourceOperation extends AbstractOperation<FrappeResponse> implements ResourceOperation {

    protected Object resource;

    public AbstractResourceOperation(
            String baseApiUrl, String doctype, OkHttpClient httpClient, Transformer transformer, String... pathParams) {
        super(baseApiUrl, doctype, httpClient, transformer, pathParams);
    }

    @Override
    public FrappeResponse doExecute(HttpUrl httpUrl) {
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        Request.Builder requestBuilder =
                new Request.Builder().url(httpUrlBuilder.build()).addHeader("Content-Type", "application/json");
        final byte[] bytes = transformResourceToBytes(resource);
        return doResourceExecute(bytes, requestBuilder);
    }

    protected byte[] transformResourceToBytes(Object resource) {
        return Optional.ofNullable(resource)
                .map(res -> {
                    if (res instanceof String) {
                        return ((String) res).getBytes(StandardCharsets.UTF_8);
                    } else {
                        return transformer.transform(res).getBytes(StandardCharsets.UTF_8);
                    }
                })
                .orElse(new byte[] {});
    }

    protected abstract FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder);

    @Override
    public <R> void withResource(R resource) {
        this.resource = resource;
    }
}
