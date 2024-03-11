package com.ozonehis.camel.frappe.sdk.internal.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class DefaultDeleteOperationTest {

    @Mock
    private okhttp3.OkHttpClient httpClient;

    @Mock
    private okhttp3.Call call;

    private DefaultDeleteOperation defaultDeleteOperation;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = org.mockito.MockitoAnnotations.openMocks(this);
        defaultDeleteOperation = new DefaultDeleteOperation("http://localhost", "doctype", httpClient, null);
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("doResourceExecute should return FrappeResponse when request is successful")
    void doResourceExecuteShouldReturnFrappeResponseWhenRequestIsSuccessful() throws Exception {
        byte[] resourceAsBytes = "test".getBytes();
        Request.Builder requestBuilder = new Request.Builder().url("http://localhost");

        when(httpClient.newCall(Mockito.any(Request.class))).thenReturn(call);
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.message("Deleted");
        responseBuilder.request(new Request.Builder().url("http://localhost").build());
        responseBuilder.body(okhttp3.ResponseBody.create("{}", okhttp3.MediaType.parse("application/json")));
        responseBuilder.protocol(okhttp3.Protocol.HTTP_1_1);
        when(call.execute()).thenReturn(responseBuilder.build());

        FrappeResponse response = defaultDeleteOperation.doResourceExecute(resourceAsBytes, requestBuilder);

        assertEquals(200, response.code());
        assertEquals("Deleted", response.message());
    }

    @Test
    @DisplayName("withName should set the name of the resource to be deleted")
    void withNameShouldSetTheNameOfTheResourceToBeUpdated() {
        String resourceName = "testResource";
        defaultDeleteOperation.withName(resourceName);
        assertEquals(resourceName, defaultDeleteOperation.getNameOfResourceToBeDeleted());
    }
}
