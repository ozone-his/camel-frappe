package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import okhttp3.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WrappedCookieTest {

    private Cookie mockCookie;

    private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
        mocksCloser = openMocks(WrappedCookieTest.class);
        this.mockCookie = mock(Cookie.class);
    }

    @AfterAll
    static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    @DisplayName("isExpired should return true when expiresAt is less than current time")
    void isExpiredShouldReturnTrueWhenExpiresAtIsLessThanCurrentTime() {
        when(mockCookie.expiresAt()).thenReturn(System.currentTimeMillis() - 11000); // 11 seconds in the past

        WrappedCookie wrappedCookie = new WrappedCookie(mockCookie);

        assertTrue(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("isExpired should return false when expiresAt is greater than current time")
    void isExpiredShouldReturnFalseWhenExpiresAtIsGreaterThanCurrentTime() {
        when(mockCookie.expiresAt()).thenReturn(System.currentTimeMillis() + 1000);

        WrappedCookie wrappedCookie = mock(WrappedCookie.class);

        assertFalse(wrappedCookie.isExpired());
    }

    @Test
    @DisplayName("unwrap should return the original list of cookies")
    void unwrapShouldReturnTheOriginalListOfCookies() {
        when(mockCookie.value()).thenReturn("cookie1; cookie2");

        WrappedCookie wrappedCookie = new WrappedCookie(mockCookie);

        assertEquals("cookie1; cookie2", wrappedCookie.unwrap());
    }
}
