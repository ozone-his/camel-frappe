package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import java.io.IOException;
import lombok.Setter;
import okhttp3.Request;
import okhttp3.Response;

@Setter
public class OAuth2Interceptor implements FrappeAuthentication {

    private OAuth2Config config;

    private OAuth2TokenManager tokenManager;

    public OAuth2Interceptor(OAuth2Config oAuth2Config) {
        this.config = oAuth2Config;
        this.tokenManager = new OAuth2TokenManager(config);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String accessToken = this.tokenManager.getAccessToken();
        Request modifiedRequest = originalRequest
                .newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
        return chain.proceed(modifiedRequest);
    }
}
