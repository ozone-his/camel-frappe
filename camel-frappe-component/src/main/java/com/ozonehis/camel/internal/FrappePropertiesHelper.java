package com.ozonehis.camel.internal;

import org.apache.camel.CamelContext;
import org.apache.camel.support.component.ApiMethodPropertiesHelper;

import com.ozonehis.camel.FrappeConfiguration;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for Frappe component.
 */
public final class FrappePropertiesHelper extends ApiMethodPropertiesHelper<FrappeConfiguration> {

    private static FrappePropertiesHelper helper;

    private FrappePropertiesHelper(CamelContext context) {
        super(context, FrappeConfiguration.class, FrappeConstants.PROPERTY_PREFIX);
    }

    public static synchronized FrappePropertiesHelper getHelper(CamelContext context) {
        if (helper == null) {
            helper = new FrappePropertiesHelper(context);
        }
        return helper;
    }
}
