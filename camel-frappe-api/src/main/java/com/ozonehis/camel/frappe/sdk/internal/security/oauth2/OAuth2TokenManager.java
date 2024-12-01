package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import lombok.Setter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OAuth2TokenManager {

    private final OAuth2Config config;

    private String accessToken;

    private long expiryTime = 0;

    @Setter
    private OkHttpClient client;

    public OAuth2TokenManager(OAuth2Config config) {
        this.config = config;
        this.client = new OkHttpClient();
    }

    public synchronized String getAccessToken() throws IOException {
        if (System.currentTimeMillis() >= expiryTime) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private void refreshAccessToken() throws IOException {
        FormBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", config.getClientId())
                .add("client_secret", config.getClientSecret())
                .add("scope", String.join(" ", config.getScopes()))
                .build();
        Request request = new Request.Builder()
                .url(config.getOauthTokenUri())
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                OAuth2Token token = new ObjectMapper().readValue(response.body().string(), OAuth2Token.class);
                long expiresIn = Long.parseLong(token.getExpiresIn());
                this.accessToken = token.getAccessToken();
                this.expiryTime = System.currentTimeMillis()
                        + (expiresIn * 1000)
                        - 5000; // Subtract 5 seconds to ensure we refresh before expiry
            } else {
                throw new AuthenticationException("Failed to fetch access token with status code: " + response.code());
            }
        }
    }
}
