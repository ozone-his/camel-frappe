package com.ozonehis.camel.frappe.sdk.internal.security;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.CookieCache;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.WrappedCookie;
import java.io.IOException;
import java.util.List;
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

    private static final String SESSION_COOKIE_NAME = "session-cookie";

    CookieCache cookieCache = CookieCache.getInstance();

    public DefaultFrappeAuthentication(String username, String password) {
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
        WrappedCookie sessionCookie = cookieCache.get(SESSION_COOKIE_NAME);
        if (sessionCookie != null && !sessionCookie.isExpired()) {
            log.debug("Session cookie found and not expired. Using it...");
            return sessionCookie.unwrap();
        } else {
            log.debug("Session cookie not found or expired. Logging in...");
            return login(incomingRequest, cookieCache);
        }
    }

    private List<String> login(Request incomingRequest, CookieCache cookieCache) throws IOException {
        var baseUrl = getBaseUrl(incomingRequest);
        Request request = buildLoginRequest(baseUrl);
        try (Response response = executeRequest(request)) {
            var cookies = response.headers("set-cookie");
            var cookieList = Cookie.parseAll(incomingRequest.url(), response.headers());
            cookieList.forEach(cookie -> {
                if (cookie.name().equalsIgnoreCase("sid") && !cookie.value().isEmpty()) {
                    cookieCache.clearExpired();
                    cookieCache.put(SESSION_COOKIE_NAME, new WrappedCookie(cookie.expiresAt(), cookies));
                }
            });
            return cookies;
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
