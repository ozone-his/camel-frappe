package com.ozonehis.camel.api.proxy;

import com.ozonehis.camel.frappe.sdk.api.FrappeClient;

public abstract class AbstractFrappeProxy {
	
	protected final FrappeClient frappeClient;
	
	public AbstractFrappeProxy(FrappeClient frappeClient) {
		this.frappeClient = frappeClient;
	}
}
