package com.ozonehis.camel;

import com.ozonehis.camel.internal.FrappeApiName;
import com.ozonehis.camel.internal.FrappePropertiesHelper;
import org.apache.camel.support.component.AbstractApiProducer;

public class FrappeProducer extends AbstractApiProducer<FrappeApiName, FrappeConfiguration> {

    public FrappeProducer(FrappeEndpoint endpoint) {
        super(endpoint, FrappePropertiesHelper.getHelper(endpoint.getCamelContext()));
    }
}
