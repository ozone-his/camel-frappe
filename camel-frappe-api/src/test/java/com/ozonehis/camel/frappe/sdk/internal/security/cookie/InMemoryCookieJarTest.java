package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class InMemoryCookieJarTest {

    @Mock
    private Cookie cookie;

    private InMemoryCookieJar inMemoryCookieJar;
	
	private static AutoCloseable mocksCloser;

    @BeforeEach
    void setUp() {
	    mocksCloser = openMocks(this);
        inMemoryCookieJar = new InMemoryCookieJar();
    }
	
	@AfterAll
	static void closeMocks() throws Exception {
		mocksCloser.close();
	}

    @Test
    @DisplayName("loadForRequest should return cookies that match the url and are not expired")
    void loadForRequestShouldReturnMatchingAndNotExpiredCookies() {
        when(cookie.matches(any(HttpUrl.class))).thenReturn(true);
        when(cookie.expiresAt()).thenReturn(System.currentTimeMillis() + 1000);
        
        inMemoryCookieJar.saveFromResponse(HttpUrl.get("http://localhost"), Collections.singletonList(cookie));
        
        assertFalse(inMemoryCookieJar.loadForRequest(HttpUrl.get("http://localhost")).isEmpty());
    }

    @Test
    @DisplayName("saveFromResponse should add cookies to the jar")
    void saveFromResponseShouldAddCookiesToTheJar() {
        when(cookie.matches(any(HttpUrl.class))).thenReturn(true);
        when(cookie.expiresAt()).thenReturn(System.currentTimeMillis() + 1000);
        
        inMemoryCookieJar.saveFromResponse(HttpUrl.get("http://localhost"), Collections.singletonList(cookie));
        
        assertFalse(inMemoryCookieJar.loadForRequest(HttpUrl.get("http://localhost")).isEmpty());
    }

    @Test
    @DisplayName("clear should remove all cookies from the jar")
    void clearShouldRemoveAllCookiesFromTheJar() {
        inMemoryCookieJar.saveFromResponse(HttpUrl.get("http://localhost"), Collections.singletonList(cookie));
        inMemoryCookieJar.clear();
        
        assertTrue(inMemoryCookieJar.loadForRequest(HttpUrl.get("http://localhost")).isEmpty());
    }

    @Test
    @DisplayName("clearExpired should remove expired cookies from the jar")
    void clearExpiredShouldRemoveExpiredCookiesFromTheJar() {
        when(cookie.expiresAt()).thenReturn(System.currentTimeMillis() - 1000);
        
        inMemoryCookieJar.saveFromResponse(HttpUrl.get("http://localhost"), Collections.singletonList(cookie));
        inMemoryCookieJar.clearExpired();
        
        assertTrue(inMemoryCookieJar.loadForRequest(HttpUrl.get("http://localhost")).isEmpty());
    }
}
