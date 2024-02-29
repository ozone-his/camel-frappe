package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class WrappedCookieTest {

    @Mock
    private Cookie cookie;

    private WrappedCookie wrappedCookie;
    
    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = openMocks(this);
        wrappedCookie = new WrappedCookie(cookie);
    }
    
    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("isExpired should return true when cookie is expired")
    void isExpiredShouldReturnTrueWhenCookieIsExpired() {
        when(cookie.expiresAt()).thenReturn(new Date().getTime() - 1000);
        assertTrue(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("isExpired should return false when cookie is not expired")
    void isExpiredShouldReturnFalseWhenCookieIsNotExpired() {
        when(cookie.expiresAt()).thenReturn(new Date().getTime() + 1000);
        assertFalse(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("unwrap should return the original cookie")
    void unwrapShouldReturnTheOriginalCookie() {
        assertEquals(cookie, wrappedCookie.unwrap());
    }

    @Test
    @DisplayName("matches should return true when cookie matches the url")
    void matchesShouldReturnTrueWhenCookieMatchesTheUrl() {
        HttpUrl url = HttpUrl.parse("http://localhost");
        when(cookie.matches(url)).thenReturn(true);
        assertTrue(wrappedCookie.matches(url));
    }

    @Test
    @DisplayName("matches should return false when cookie does not match the url")
    void matchesShouldReturnFalseWhenCookieDoesNotMatchTheUrl() {
        HttpUrl url = HttpUrl.parse("http://localhost");
        when(cookie.matches(url)).thenReturn(false);
        assertFalse(wrappedCookie.matches(url));
    }
}
