package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.IOException;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class OAuth2InterceptorTest {

    @Mock
    private OAuth2Config mockConfig;

    @Mock
    private OAuth2TokenManager mockTokenManager;

    @Mock
    private Chain mockChain;

    private OAuth2Interceptor interceptor;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = openMocks(this);

        when(mockConfig.getClientId()).thenReturn("testClientId");
        when(mockConfig.getClientSecret()).thenReturn("testClientSecret");
        when(mockConfig.getScopes()).thenReturn(new String[] {"testScope"});
        when(mockConfig.getOauthTokenUri()).thenReturn("http://test.com/token");

        interceptor = new OAuth2Interceptor(mockConfig);
        interceptor.setTokenManager(mockTokenManager);
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("Should add Authorization header")
    void shouldAddAuthorizationHeader() throws Exception {
        // Arrange
        when(mockTokenManager.getAccessToken()).thenReturn("testAccessToken");
        Request originalRequest = new Request.Builder().url("http://test.com").build();
        when(mockChain.request()).thenReturn(originalRequest);

        // Act
        interceptor.intercept(mockChain);

        // Verify
        verify(mockChain, times(1))
                .proceed(argThat(request -> "Bearer testAccessToken".equals(request.header("Authorization"))));
    }

    @Test
    @DisplayName("Should handle TokenManagerException")
    void shouldHandleTokenManagerException() throws Exception {
        // Arrange
        when(mockTokenManager.getAccessToken()).thenThrow(new IOException("Failed to get access token"));
        Request originalRequest = new Request.Builder().url("http://test.com").build();
        when(mockChain.request()).thenReturn(originalRequest);

        // Act & Verify
        assertThrows(IOException.class, () -> interceptor.intercept(mockChain));
    }
}
