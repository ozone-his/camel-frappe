package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import java.util.ArrayList;
import java.util.List;
import lombok.Synchronized;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

public class InMemoryCookieJar implements CookieJar {

    private final List<WrappedCookie> cookiesCache = new ArrayList<>();

    @NotNull @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return this.cookiesCache.stream()
                .filter(cookie -> cookie.matches(httpUrl) && !cookie.isExpired())
                .collect(ArrayList::new, (list, cookie) -> list.add(cookie.unwrap()), ArrayList::addAll);
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        this.cookiesCache.removeAll(cookies.stream().map(WrappedCookie::new).toList());
        this.cookiesCache.addAll(cookies.stream().map(WrappedCookie::new).toList());
    }

    @Synchronized
    public void clear() {
        this.cookiesCache.clear();
    }

    @Synchronized
    public void clearExpired() {
        this.cookiesCache.removeIf(WrappedCookie::isExpired);
    }
}
