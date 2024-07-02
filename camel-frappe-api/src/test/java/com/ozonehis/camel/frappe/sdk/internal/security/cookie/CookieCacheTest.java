package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Objects;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CookieCacheTest {

    private static final Logger log = LoggerFactory.getLogger(CookieCacheTest.class);

    private static AutoCloseable mocksCloser;

    @BeforeAll
    static void setUp() {
        mocksCloser = openMocks(CookieCacheTest.class);
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("put should store the cookie in the cache")
    void putShouldStoreTheCookieInTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie = new WrappedCookie(Cookie.parse(
                Objects.requireNonNull(HttpUrl.parse("https://example.com")),
                "testCookie=cookie1; testCookie2=cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        assertEquals(wrappedCookie, cookieCache.get("testCookie"));
    }

    @Test
    @DisplayName("get should return the correct cookie from the cache")
    void getShouldReturnTheCorrectCookieFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie = new WrappedCookie(Cookie.parse(
                Objects.requireNonNull(HttpUrl.parse("https://example.com")),
                "testCookie=cookie1; testCookie2=cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        assertEquals(wrappedCookie, cookieCache.get("testCookie"));
    }

    @Test
    @DisplayName("clear should remove all cookies from the cache")
    void clearShouldRemoveAllCookiesFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();
        WrappedCookie wrappedCookie = new WrappedCookie(Cookie.parse(
                Objects.requireNonNull(HttpUrl.parse("https://example.com")),
                "testCookie=cookie1; testCookie2=cookie2"));
        cookieCache.put("testCookie", wrappedCookie);
        cookieCache.clear();
        assertNull(cookieCache.get("testCookie"));
        assertNull(cookieCache.get("testCookie2"));
    }

    @Test
    @DisplayName("clearExpired should remove only expired cookies from the cache")
    void clearExpiredShouldRemoveOnlyExpiredCookiesFromTheCache() {
        CookieCache cookieCache = CookieCache.getInstance();

        var expiredCookie = Mockito.mock(Cookie.class);
        WrappedCookie expiredWrappedCookie = new WrappedCookie(expiredCookie);

        var validCookie = Mockito.mock(Cookie.class);
        WrappedCookie validWrappedCookie = new WrappedCookie(validCookie);

        cookieCache.put("expiredCookie", expiredWrappedCookie);
        cookieCache.put("validCookie", validWrappedCookie);

        when(expiredCookie.expiresAt()).thenReturn(System.currentTimeMillis() - 1000);
        when(validCookie.expiresAt()).thenReturn(System.currentTimeMillis() + 1000);

        cookieCache.clearExpired();

        assertNull(cookieCache.get("expiredCookie"));
        assertEquals(validWrappedCookie, cookieCache.get("validCookie"));
    }
}
