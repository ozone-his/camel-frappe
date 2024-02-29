package com.ozonehis.camel;

import org.apache.camel.support.component.AbstractApiProducer;

import com.ozonehis.camel.internal.FrappeApiName;
import com.ozonehis.camel.internal.FrappePropertiesHelper;

public class FrappeProducer extends AbstractApiProducer<FrappeApiName, FrappeConfiguration> {

    public FrappeProducer(FrappeEndpoint endpoint) {
        super(endpoint, FrappePropertiesHelper.getHelper(endpoint.getCamelContext()));
    }
}
