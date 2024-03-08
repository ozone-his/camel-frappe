package com.ozonehis.camel;

import com.ozonehis.camel.internal.FrappeApiName;
import org.apache.camel.Processor;
import org.apache.camel.support.component.AbstractApiConsumer;

public class FrappeConsumer extends AbstractApiConsumer<FrappeApiName, FrappeConfiguration> {

    public FrappeConsumer(FrappeEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }
}
