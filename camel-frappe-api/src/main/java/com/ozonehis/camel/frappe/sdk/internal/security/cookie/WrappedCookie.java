package com.ozonehis.camel.frappe.sdk.internal.security.cookie;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class WrappedCookie {

    private long expiresAt;

    private List<String> cookies;

    public boolean isExpired() {
        return expiresAt < System.currentTimeMillis();
    }

    public List<String> unwrap() {
        return cookies;
    }
}
