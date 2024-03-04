package com.ozonehis.camel.frappe.sdk.api.operation;

public interface PutOperation extends ResourceOperation{
	
	/**
	 * This method sets the name of the resource to be updated.
	 *
	 * @param name The name of the resource to be updated.
	 * @return The PutOperation instance.
	 */
	PutOperation withName(String name);
	
}
