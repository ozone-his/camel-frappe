package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WrappedCookieTest {

    @Test
    @DisplayName("isExpired should return true when expiresAt is less than current time")
    void isExpiredShouldReturnTrueWhenExpiresAtIsLessThanCurrentTime() {
        WrappedCookie wrappedCookie =
                new WrappedCookie(System.currentTimeMillis() - 1000, List.of("cookie1", "cookie2"));
        assertTrue(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("isExpired should return false when expiresAt is greater than current time")
    void isExpiredShouldReturnFalseWhenExpiresAtIsGreaterThanCurrentTime() {
        WrappedCookie wrappedCookie =
                new WrappedCookie(System.currentTimeMillis() + 1000, List.of("cookie1", "cookie2"));
        assertFalse(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("unwrap should return the original list of cookies")
    void unwrapShouldReturnTheOriginalListOfCookies() {
        List<String> originalCookies = List.of("cookie1", "cookie2");
        WrappedCookie wrappedCookie = new WrappedCookie(System.currentTimeMillis() + 1000, originalCookies);
        assertEquals(originalCookies, wrappedCookie.unwrap());
    }
}
