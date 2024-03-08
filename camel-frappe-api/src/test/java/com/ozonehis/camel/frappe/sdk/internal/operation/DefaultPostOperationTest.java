package com.ozonehis.camel.frappe.sdk.internal.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class DefaultPostOperationTest {

    @Mock
    private okhttp3.OkHttpClient httpClient;

    @Mock
    private okhttp3.Call call;

    private DefaultPostOperation defaultPostOperation;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = org.mockito.MockitoAnnotations.openMocks(this);
        defaultPostOperation = new DefaultPostOperation("http://localhost", "doctype", httpClient, null);
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("doResourceExecute should return FrappeResponse when request is successful")
    void doResourceExecuteShouldReturnFrappeResponseWhenRequestIsSuccessful() throws IOException {
        byte[] resourceAsBytes = "test".getBytes();
        Request.Builder requestBuilder = new Request.Builder().url("http://localhost");

        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.message("OK");
        responseBuilder.request(new Request.Builder().url("http://localhost").build());
        responseBuilder.body(okhttp3.ResponseBody.create("{}", okhttp3.MediaType.parse("application/json")));
        responseBuilder.protocol(okhttp3.Protocol.HTTP_1_1);
        when(call.execute()).thenReturn(responseBuilder.build());

        FrappeResponse response = defaultPostOperation.doResourceExecute(resourceAsBytes, requestBuilder);

        assertNotNull(response);
        assertEquals(200, response.code());
        assertEquals("OK", response.message());
    }

    @Test
    @DisplayName("doResourceExecute should throw IOException when request fails")
    void doResourceExecuteShouldThrowIOExceptionWhenRequestFails() throws IOException {
        byte[] resourceAsBytes = "test".getBytes();
        Request.Builder requestBuilder = new Request.Builder().url("http://localhost");

        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenThrow(IOException.class);

        assertThrows(
                FrappeClientException.class,
                () -> defaultPostOperation.doResourceExecute(resourceAsBytes, requestBuilder));
    }
}
