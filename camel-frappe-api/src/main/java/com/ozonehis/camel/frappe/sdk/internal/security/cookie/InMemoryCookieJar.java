package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import java.util.ArrayList;
import java.util.List;
import lombok.Synchronized;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

public class InMemoryCookieJar implements CookieJar {

    private final List<WrappedCookie> cookieCache = new ArrayList<>();

    @NotNull @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return this.cookieCache.stream()
                .filter(cookie -> cookie.matches(httpUrl) && !cookie.isExpired())
                .collect(ArrayList::new, (list, cookie) -> list.add(cookie.unwrap()), ArrayList::addAll);
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        List<WrappedCookie> wrappedCookies =
                cookies.stream().map(WrappedCookie::new).toList();
        this.clear();
        this.cookieCache.addAll(wrappedCookies);
    }

    @Synchronized
    public void clear() {
        this.cookieCache.clear();
    }

    @Synchronized
    public void clearExpired() {
        this.cookieCache.removeIf(WrappedCookie::isExpired);
    }
}
