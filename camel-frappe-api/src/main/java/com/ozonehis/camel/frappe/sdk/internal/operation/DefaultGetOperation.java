package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Slf4j
public class DefaultGetOperation extends AbstractOperation<FrappeResponse> implements GetOperation {

    protected final List<String> fields = new ArrayList<>();

    protected List<List<String>> filters = new ArrayList<>();

    public DefaultGetOperation(
            String baseApiUrl, String path, OkHttpClient httpClient, Transformer transformer, String... pathParams) {
        super(baseApiUrl, path, httpClient, transformer, pathParams);
    }

    @Override
    protected FrappeResponse doExecute(HttpUrl httpUrl) {
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        if (!fields.isEmpty()) {
            String fieldsAsString =
                    fields.stream().map(field -> "\"" + field + "\"").collect(Collectors.joining(", ", "[", "]"));
            httpUrlBuilder.addQueryParameter("fields", fieldsAsString);
        }
        convertFiltersToString(filters)
                .ifPresent(filtersAsString -> httpUrlBuilder.addQueryParameter("filters", filtersAsString));
        okhttp3.Response response = onHttpResponse(() -> httpClient
                .newCall(new Request.Builder().url(httpUrlBuilder.build()).get().build())
                .execute());

        return new DefaultFrappeResponse(response, transformer);
    }

    @Override
    public GetOperation withFields(List<String> fields) {
        this.fields.addAll(fields);
        return this;
    }

    @Override
    public GetOperation withField(String field) {
        this.fields.add(field);
        return this;
    }

    @Override
    public GetOperation withFilters(List<List<String>> filters) {
        this.filters.addAll(filters);
        return this;
    }

    @Override
    public GetOperation withFilter(List<String> filter) {
        this.filters.add(filter);
        return this;
    }
}
