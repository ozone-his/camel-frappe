package com.ozonehis.camel.frappe.sdk.internal.security;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.CookieCache;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.WrappedCookie;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class DefaultFrappeAuthentication implements FrappeAuthentication {

    private final String username;

    private final String password;

    public DefaultFrappeAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NotNull @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request original = chain.request();
        final Request authorized = original.newBuilder()
                .addHeader("Cookie", getSessionCookies(original))
                .build();
        return chain.proceed(authorized);
    }

    public String getSessionCookies(Request incomingRequest) throws IOException {
        String[] cookieNames = {"sid", "system_user", "full_name", "user_id", "user_image"};
        StringBuilder cookies = new StringBuilder();
        for (String cookieName : cookieNames) {
            WrappedCookie cookie = CookieCache.getInstance().get(cookieName);
            if (cookie != null && !cookie.isExpired()) {
                cookies.append(cookieName).append("=").append(cookie.unwrap()).append("; ");
            }
        }
        // Make sure the sid cookie is included
        if (!cookies.toString().contains("sid") || cookies.isEmpty()) {
            if (log.isDebugEnabled()) log.debug("SID session cookie expired, logging in again...");
            login(incomingRequest);
            return getSessionCookies(incomingRequest);
        }
        return cookies.toString();
    }

    private void login(Request incomingRequest) throws IOException {
        Request request = buildLoginRequest(getBaseUrl(incomingRequest));
        try (Response response = executeRequest(request)) {
            CookieCache.getInstance().clearExpired();
            Cookie.parseAll(incomingRequest.url(), response.headers())
                    .forEach(cookie -> CookieCache.getInstance().put(cookie.name(), new WrappedCookie(cookie)));
        } catch (IOException e) {
            throw new AuthenticationException("Error while logging in", e);
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
