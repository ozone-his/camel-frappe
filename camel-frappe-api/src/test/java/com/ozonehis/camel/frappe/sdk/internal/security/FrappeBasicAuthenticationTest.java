package com.ozonehis.camel.frappe.sdk.internal.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class FrappeBasicAuthenticationTest {

    @Mock
    private Request request;

    private FrappeBasicAuthentication frappeBasicAuthentication;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = openMocks(this);
        when(request.url()).thenReturn(HttpUrl.get("http://localhost:8080"));
        frappeBasicAuthentication = new FrappeBasicAuthentication("username", "password");
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("getBaseUrl should return correct base url")
    void getBaseUrlShouldReturnCorrectBaseUrl() {
        when(request.url()).thenReturn(HttpUrl.get("http://localhost:8080"));
        assertEquals("http://localhost:8080", frappeBasicAuthentication.getBaseUrl(request));
    }
}
