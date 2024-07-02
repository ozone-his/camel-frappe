package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.Cookie;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class WrappedCookie {

    private Cookie cookie;

    public boolean isExpired() {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public String unwrap() {
        return cookie.value();
    }
}
