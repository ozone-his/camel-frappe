package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import java.util.ArrayList;
import java.util.List;
import lombok.Synchronized;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

public class InMemoryCookieJar implements CookieJar {

    private final List<Cookie> cookiesInMemoryStore = new ArrayList<>();

    @NotNull @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        this.clearExpired();
        return this.cookiesInMemoryStore.stream()
                .filter(cookie -> cookie.matches(httpUrl))
                .toList();
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        this.cookiesInMemoryStore.addAll(cookies);
    }

    @Synchronized
    public void clear() {
        this.cookiesInMemoryStore.clear();
    }

    @Synchronized
    public void clearExpired() {
        this.cookiesInMemoryStore.removeIf(cookie -> cookie.expiresAt() < System.currentTimeMillis());
    }
}
