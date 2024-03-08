package com.ozonehis.camel.frappe.sdk.internal.operation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class DefaultGetOperationTest {

    @Mock
    private OkHttpClient httpClient;

    @Mock
    private Transformer transformer;

    @Mock
    private Call call;

    private static AutoCloseable mocksCloser;

    private DefaultGetOperation defaultGetOperation;

    @BeforeEach
    void setUp() throws IOException {
        mocksCloser = openMocks(this);
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.message("OK");
        responseBuilder.request(new Request.Builder().url("http://localhost").build());
        responseBuilder.body(okhttp3.ResponseBody.create("{}", okhttp3.MediaType.parse("application/json")));
        responseBuilder.protocol(okhttp3.Protocol.HTTP_1_1);
        when(call.execute()).thenReturn(responseBuilder.build());

        defaultGetOperation = new DefaultGetOperation("http://localhost", "/path", httpClient, transformer, "param");
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("execute should throw IllegalArgumentException when URL is invalid")
    void executeShouldThrowIllegalArgumentExceptionWhenUrlIsInvalid() {
        defaultGetOperation = new DefaultGetOperation("", "/path", httpClient, transformer, "param");
        assertThrows(IllegalArgumentException.class, defaultGetOperation::execute);
    }

    @Test
    @DisplayName("execute should add fields to query parameters when fields are not empty")
    void executeShouldAddFieldsToQueryParametersWhenFieldsAreNotEmpty() {
        defaultGetOperation.withFields(Arrays.asList("field1", "field2"));

        var response = defaultGetOperation.execute();

        assertNotNull(response);
        verify(httpClient).newCall(any(Request.class));
    }

    @Test
    @DisplayName("execute should not add fields to query parameters when fields are empty")
    void executeShouldNotAddFieldsToQueryParametersWhenFieldsAreEmpty() {
        defaultGetOperation.withFields(Collections.emptyList());
        defaultGetOperation.execute();

        verify(httpClient).newCall(any(Request.class));
    }

    @Test
    @DisplayName("execute should add filters to query parameters when filters are not empty")
    void executeShouldAddFiltersToQueryParametersWhenFiltersAreNotEmpty() {
        defaultGetOperation.withFilters(Arrays.asList(Arrays.asList("filter1", "filter2")));
        defaultGetOperation.execute();

        verify(httpClient).newCall(any(Request.class));
    }

    @Test
    @DisplayName("execute should not add filters to query parameters when filters are empty")
    void executeShouldNotAddFiltersToQueryParametersWhenFiltersAreEmpty() {
        defaultGetOperation.withFilters(Collections.emptyList());
        defaultGetOperation.execute();

        verify(httpClient).newCall(any(Request.class));
    }
}
