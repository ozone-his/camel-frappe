package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class OAuth2TokenManagerTest {

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;

    private OAuth2TokenManager tokenManager;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() throws IOException {
        mocksCloser = openMocks(this);
        OAuth2Config config = new OAuth2Config(
                "clientId", "clientSecret", "http://oauth.token.uri", new String[] {"scope1", "scope2"});
        tokenManager = new OAuth2TokenManager(config);

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
    }

    @AfterAll
    public static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("Should refresh access token when expired")
    void shouldRefreshAccessTokenWhenExpired() throws IOException {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"access_token\":\"newAccessToken\",\"expires_in\":\"3600\"}");
        tokenManager.setClient(mockClient);

        // Act
        String accessToken = tokenManager.getAccessToken();

        // Verify
        assertNotNull(accessToken);
        assertEquals("newAccessToken", accessToken);
    }

    @Test
    @DisplayName("Should reuse access token if not expired")
    void shouldReuseAccessTokenIfNotExpired() throws IOException {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"access_token\":\"accessToken\",\"expires_in\":\"3600\"}");
        tokenManager.setClient(mockClient);

        // First call to get a new token
        String firstAccessToken = tokenManager.getAccessToken();
        // Second call should reuse the same token
        String secondAccessToken = tokenManager.getAccessToken();

        // Verify
        assertNotNull(firstAccessToken);
        // Assert that the same token is reused
        assertEquals(firstAccessToken, secondAccessToken);
    }

    @Test
    @DisplayName("Should throw AuthenticationException on unsuccessful response")
    void shouldThrowAuthenticationExceptionOnUnsuccessfulResponse() {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(false);
        tokenManager.setClient(mockClient);

        // Act & Verify
        assertThrows(AuthenticationException.class, () -> tokenManager.getAccessToken());
    }

    @Test
    @DisplayName("Should throw AuthenticationException when response body is null")
    void shouldThrowAuthenticationExceptionWhenResponseBodyIsNull() {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(null);
        tokenManager.setClient(mockClient);

        // Act & Verify
        assertThrows(AuthenticationException.class, () -> tokenManager.getAccessToken());
    }
}
