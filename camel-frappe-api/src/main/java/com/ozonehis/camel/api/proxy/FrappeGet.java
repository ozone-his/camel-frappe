package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;
import com.ozonehis.camel.frappe.sdk.api.operation.GetOperation;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *  This class is a proxy to the Frappe SDKS's GetOperation.
 */
public class FrappeGet {
	
	private final FrappeClient frappeClient;
	
	public FrappeGet(FrappeClient frappeClient) {
		this.frappeClient = frappeClient;
	}
	
	/**
	 * This method retrieves the resource.
	 *
	 * @param doctype The doctype.
	 * @param fields The fields to be retrieved.
	 * @param filters The filters to be applied.
	 * @param queryParams The query parameters.
	 * @return The resource.
	 */
	public InputStream resource(String doctype, List<String> fields, List<List<String>> filters, Map<String, Object> queryParams) {
		return newGetOperation(doctype, fields, filters, queryParams).execute().read();
	}
	
	/**
	 * This method creates a new GetOperation instance.
	 *
	 * @param doctype The doctype.
	 * @param fields The fields to be retrieved.
	 * @param filters The filters to be applied.
	 * @param queryParams The query parameters.
	 * @return The GetOperation instance.
	 */
	@SuppressWarnings("unchecked")
	protected GetOperation newGetOperation(
			String doctype, List<String> fields, List<List<String>> filters, Map<String, Object> queryParams) {
		GetOperation getOperation = frappeClient.get(doctype);
		if (fields != null && !fields.isEmpty()) {
			getOperation.withFields(fields);
		}
		
		if (filters != null && !filters.isEmpty()) {
			getOperation.withFilters(filters);
		}
		
		if (queryParams != null && !queryParams.isEmpty()) {
			for (Map.Entry<String, Object> queryParam : queryParams.entrySet()) {
				if (queryParam.getValue() instanceof List) {
					for (String queryValue : (List<String>) queryParam.getValue()) {
						getOperation.withParameter(queryParam.getKey(), queryValue);
					}
				} else {
					getOperation.withParameter(queryParam.getKey(), (String) queryParam.getValue());
				}
			}
		}
		
		return getOperation;
	}
}
