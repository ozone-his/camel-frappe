package com.ozonehis.camel.frappe.sdk.internal.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OAuth2Config {

    private String clientId;

    private String clientSecret;

    private String oauthTokenUri;

    private String[] scopes;
}
