package com.ozonehis.camel.frappe.sdk.api.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;

/**
 * This interface represents a resource operation e.g. a GET, POST, PUT, DELETE operation.
 */
public interface ResourceOperation extends ParameterizedOperation<FrappeResponse>{
	
	/**
	 * This method sets the resource.
	 *
	 * @param resource The resource to set.
	 */
	void withResource(Object resource);
}
