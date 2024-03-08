package com.ozonehis.camel.frappe.sdk.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import java.io.InputStream;
import okhttp3.Response;
import okhttp3.ResponseBody;

public record DefaultFrappeResponse(Response response, Transformer transformer) implements FrappeResponse {

    @Override
    public <T> T returnAs(Class<T> returnType) {
        if (response.body() == null) {
            return null;
        }
        return transformer.transform(response.body().byteStream(), returnType);
    }

    @Override
    public <T> T returnAs(TypeReference<T> typeReference) {
        if (response.body() == null) {
            return null;
        }
        return transformer.transform(response.body().byteStream(), typeReference);
    }

    @Override
    public InputStream read() {
        if (response.body() != null) {
            return response.body().byteStream();
        }
        return null;
    }

    @Override
    public String getUrl() {
        return response.request().url().toString();
    }

    @Override
    public int code() {
        return response.code();
    }

    @Override
    public ResponseBody responseBody() {
        return response.body();
    }

    @Override
    public void close() {
        response.close();
    }
}
