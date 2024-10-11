package com.ozonehis.camel.frappe.sdk.internal.security;

import com.ozonehis.camel.frappe.sdk.api.security.FrappeAuthentication;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.CookieCache;
import com.ozonehis.camel.frappe.sdk.internal.security.cookie.WrappedCookie;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
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
        // Cookies to be included in the request
        List<String> cookieSet = List.of("sid", "path", "domain", "Expires", "user_id", "full_name", "user_image");
        Collection<WrappedCookie> wrappedCookies = CookieCache.getInstance().getAll();
        if (wrappedCookies.isEmpty()) {
            login(incomingRequest);
            return getSessionCookies(incomingRequest);
        }
        StringJoiner cookies = new StringJoiner("; ");
        for (WrappedCookie wrappedCookie : wrappedCookies) {
            if (cookieSet.contains(wrappedCookie.getCookie().name())) {
                cookies.add(wrappedCookie.getCookie().name() + "=" + wrappedCookie.unwrap());
            }
        }
        return cookies.toString();
    }

    private void login(Request incomingRequest) throws IOException {
        Request request = buildLoginRequest(getBaseUrl(incomingRequest));
        var cookieCache = CookieCache.getInstance();
        try (Response response = executeRequest(request)) {
            cookieCache.clearExpired();
            Cookie.parseAll(incomingRequest.url(), response.headers())
                    .forEach(cookie -> cookieCache.put(cookie.name(), new WrappedCookie(cookie)));
            // Save the domain cookie.
            // This is necessary because the domain cookie isn't included in the response headers.
            cookieCache.put(
                    "domain",
                    new WrappedCookie(Cookie.parse(
                            incomingRequest.url(),
                            "domain=" + incomingRequest.url().host())));
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
