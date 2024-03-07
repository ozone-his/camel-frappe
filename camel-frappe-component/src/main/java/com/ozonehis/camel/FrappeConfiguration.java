package com.ozonehis.camel;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.internal.FrappeApiName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.spi.Configurer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;

@Getter
@Setter
@UriParams
@Configurer(extended = true)
public class FrappeConfiguration {
    
    @UriParam(description = "Base URL of the Frappe instance(Or implementation of frappe e.g ERPNext)")
    private String baseApiUrl;
    
    @UriParam(description = "Frappe username", secret = true, label = "security")
    private String username;
    
    @UriParam(description = "Password of the Frappe username", secret = true, label = "security")
    private String password;
    
    @UriPath(description = "API operation (e.g., get, post, put, delete, etc.)")
    @Metadata(required = true)
    private FrappeApiName apiName;
    
    @UriPath(description = "Subject of the API operation (e.g., resource or method)")
    @Metadata(required = true)
    private String methodName;
    
    @UriParam(label = "advanced",
            description = "References a user-defined com.ozonehis.camel.frappe.sdk.api.FrappeClient. This option is mutually exclusive to the baseApiUrl, username, password, and personalAccessToken options")
    private FrappeClient frappeClient;

}
