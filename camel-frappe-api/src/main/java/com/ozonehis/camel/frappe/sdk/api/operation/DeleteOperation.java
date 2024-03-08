package com.ozonehis.camel.frappe.sdk.api.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;

public interface DeleteOperation extends ParameterizedOperation<FrappeResponse> {

    /**
     * This method sets the name of the resource to be deleted.
     *
     * @param name The name of the resource to be deleted.
     * @return The DeleteOperation instance.
     */
    DeleteOperation withName(String name);
}
