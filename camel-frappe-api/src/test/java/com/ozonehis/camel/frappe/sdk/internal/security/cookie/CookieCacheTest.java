package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieCacheTest {

    @Test
    @DisplayName("put should store the cookie in the cache")
    void putShouldStoreTheCookieInTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie =
                new WrappedCookie(System.currentTimeMillis() + 1000, List.of("cookie1", "cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        assertEquals(wrappedCookie, cookieCache.get("testCookie"));
    }

    @Test
    @DisplayName("get should return the correct cookie from the cache")
    void getShouldReturnTheCorrectCookieFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie =
                new WrappedCookie(System.currentTimeMillis() + 1000, List.of("cookie1", "cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        assertEquals(wrappedCookie, cookieCache.get("testCookie"));
    }

    @Test
    @DisplayName("clear should remove all cookies from the cache")
    void clearShouldRemoveAllCookiesFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie =
                new WrappedCookie(System.currentTimeMillis() + 1000, List.of("cookie1", "cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        cookieCache.clear();
        assertNull(cookieCache.get("testCookie"));
    }

    @Test
    @DisplayName("clearExpired should remove only expired cookies from the cache")
    void clearExpiredShouldRemoveOnlyExpiredCookiesFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie expiredCookie =
                new WrappedCookie(System.currentTimeMillis() - 1000, List.of("cookie1", "cookie2"));
        WrappedCookie validCookie = new WrappedCookie(System.currentTimeMillis() + 1000, List.of("cookie3", "cookie4"));
        cookieCache.put("expiredCookie", expiredCookie);
        cookieCache.put("validCookie", validCookie);
        cookieCache.clearExpired();
        assertNull(cookieCache.get("expiredCookie"));
        assertEquals(validCookie, cookieCache.get("validCookie"));
    }
}
