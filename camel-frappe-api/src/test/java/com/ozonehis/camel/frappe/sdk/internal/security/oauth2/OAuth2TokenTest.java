package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuth2TokenTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should deserialize from JSON")
    void shouldDeserializeFromJson() throws Exception {
        // Arrange & Act
        String json = "{\"access_token\":\"abc123\",\"expires_in\":\"3600\",\"token_type\":\"Bearer\"}";
        OAuth2Token token = objectMapper.readValue(json, OAuth2Token.class);

        // Verify
        assertEquals("abc123", token.getAccessToken());
        assertEquals("3600", token.getExpiresIn());
        assertEquals("Bearer", token.getTokenType());
    }

    @Test
    @DisplayName("Should ignore unknown properties")
    void shouldIgnoreUnknownProperties() throws Exception {
        // Arrange & Act
        String json =
                "{\"access_token\":\"abc123\",\"expires_in\":\"3600\",\"token_type\":\"Bearer\",\"unknown_prop\":\"value\"}";
        OAuth2Token token = objectMapper.readValue(json, OAuth2Token.class);

        // Verify
        assertEquals("abc123", token.getAccessToken());
    }

    @Test
    @DisplayName("Should handle null JSON")
    void shouldHandleNullJson() throws Exception {
        // Arrange & Act
        String json = "{}";
        OAuth2Token token = objectMapper.readValue(json, OAuth2Token.class);

        // Verify
        assertNull(token.getAccessToken());
        assertNull(token.getExpiresIn());
        assertNull(token.getTokenType());
    }
}
