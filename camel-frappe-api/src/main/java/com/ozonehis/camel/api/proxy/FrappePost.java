package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.PostOperation;

import java.io.InputStream;

public class FrappePost extends AbstractFrappeProxy {
	
	public FrappePost(FrappeClient frappeClient) {
		super(frappeClient);
	}
	
	public InputStream resource(String doctype, Object resource) {
		PostOperation postOperation = frappeClient.post(doctype);
		
		if (resource != null) {
			postOperation.withResource(resource);
		}
		
		return postOperation.execute().read();
	}
}
