package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;

import java.io.InputStream;
import java.util.Map;

public class FrappePut extends AbstractFrappeProxy {
	
	public FrappePut(FrappeClient frappeClient) {
		super(frappeClient);
	}
	
	public InputStream resource(String doctype, String name, Object resource, Map<String, Object> queryParams) {
		ResourceOperation putOperation = frappeClient.put(doctype, name);
		if (queryParams != null) {
			for (Map.Entry<String, Object> queryParam : queryParams.entrySet()) {
				putOperation.withParameter(queryParam.getKey(), (String) queryParam.getValue());
			}
		}
		
		if (resource != null) {
			putOperation.withResource(resource);
		}
		
		return putOperation.execute().read();
	}
}
