package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class FrappePost extends AbstractFrappeProxy {
	
	public FrappePost(FrappeClient frappeClient) {
		super(frappeClient);
	}
	
	@SuppressWarnings("unchecked")
	public InputStream resource(String doctype,  Object resource, Map<String, Object> queryParams) {
		ResourceOperation postOperation = frappeClient.post(doctype);
		if (queryParams != null) {
			for (Map.Entry<String, Object> queryParam : queryParams.entrySet()) {
				if (queryParam.getValue() instanceof List) {
					for (String queryValue : (List<String>) queryParam.getValue()) {
						postOperation.withParameter(queryParam.getKey(), queryValue);
					}
				} else {
					postOperation.withParameter(queryParam.getKey(), (String) queryParam.getValue());
				}
			}
		}
		
		if (resource != null) {
			postOperation.withResource(resource);
		}
		
		return postOperation.execute().read();
	}
}
