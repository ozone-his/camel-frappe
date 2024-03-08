package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.PutOperation;
import java.io.InputStream;

public class FrappePut extends AbstractFrappeProxy {

    public FrappePut(FrappeClient frappeClient) {
        super(frappeClient);
    }

    public InputStream resource(String doctype, String name, Object resource) {
        PutOperation putOperation = frappeClient.put(doctype);

        if (resource != null) {
            putOperation.withResource(resource);
        }

        if (name != null) {
            putOperation.withName(name);
        }

        return putOperation.execute().read();
    }
}
