package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class WrappedCookie {

    private Cookie cookie;

    public boolean isExpired() {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public Cookie unwrap() {
        return cookie;
    }

    public boolean matches(HttpUrl url) {
        return cookie.matches(url);
    }
}
