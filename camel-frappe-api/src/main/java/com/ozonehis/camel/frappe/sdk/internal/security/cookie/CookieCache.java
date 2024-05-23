package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import java.util.concurrent.ConcurrentHashMap;
import lombok.NoArgsConstructor;

/**
 * A cache for storing cookies.
 */
@NoArgsConstructor
public class CookieCache {

    private static CookieCache instance = null;

    private final ConcurrentHashMap<String, WrappedCookie> cookieStore = new ConcurrentHashMap<>();

    public static CookieCache getInstance() {
        if (instance == null) {
            instance = new CookieCache();
        }
        return instance;
    }

    /**
     * Put cookies by name.
     *
     * @param cookieName the name of the cookie
     * @param cookies the wrappedCookies
     */
    public void put(String cookieName, WrappedCookie cookies) {
        cookieStore.put(cookieName, cookies);
    }

    /**
     * Get cookies by name.
     *
     * @param cookieName the name of the cookie
     * @return the wrappedCookies
     */
    public WrappedCookie get(String cookieName) {
        return cookieStore.get(cookieName);
    }

    /**
     * Clear all cookies from the cache.
     */
    public void clear() {
        cookieStore.clear();
    }

    /**
     * Clear expired cookies from the cache.
     */
    public void clearExpired() {
        cookieStore.forEach((key, value) -> {
            if (value.isExpired()) {
                cookieStore.remove(key);
            }
        });
    }
}
