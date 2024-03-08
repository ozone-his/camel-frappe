package com.ozonehis.camel.frappe.sdk.internal.security;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class FrappeBasicAuthentication implements FrappeAuthentication {

    private final String username;

    private final String password;

    public FrappeBasicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NotNull @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request original = chain.request();
        final Request authorized = original.newBuilder()
                .addHeader("Cookie", String.join("; ", getSessionCookie(original)))
                .build();
        return chain.proceed(authorized);
    }

    public List<String> getSessionCookie(Request incomingRequest) throws IOException {
        var baseUrl = getBaseUrl(incomingRequest);
        Request request = buildLoginRequest(baseUrl);
        try (Response response = executeRequest(request)) {
            return extractCookies(response);
        }
    }

    private Request buildLoginRequest(String baseUrl) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body =
                RequestBody.Companion.create("{\"usr\":\"" + username + "\", \"pwd\":\"" + password + "\"}", mediaType);
        return new Request.Builder()
                .url(baseUrl + "/api/method/login")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();
    }

    private Response executeRequest(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }

    private List<String> extractCookies(Response response) {
        if (response.isSuccessful()) {
            log.info("Logged in successfully");
            return response.headers("set-cookie");
        } else {
            log.error("Failed to login, {}", response.body());
            return null;
        }
    }

    public String getBaseUrl(Request request) {
        int port = request.url().port();
        String scheme = request.url().scheme();
        if ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) {
            return scheme + "://" + request.url().host();
        } else {
            return scheme + "://" + request.url().host() + ":" + port;
        }
    }
}
