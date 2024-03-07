package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.DeleteOperation;

import java.io.InputStream;

public class FrappeDelete extends AbstractFrappeProxy {
	
	public FrappeDelete(FrappeClient frappeClient) {
		super(frappeClient);
	}
	
	public InputStream resource(String doctype, String name) {
		DeleteOperation deleteOperation = frappeClient.delete(doctype);
		if (name != null) {
			deleteOperation.withName(name);
		}
		
		return deleteOperation.execute().read();
	}
}
