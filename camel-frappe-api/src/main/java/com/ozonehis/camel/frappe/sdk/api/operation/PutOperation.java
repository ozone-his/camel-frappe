package com.ozonehis.camel.frappe.sdk.api.operation;

/**
 * This interface represents a PUT operation.
 */
public interface PutOperation extends ResourceOperation {

    /**
     * This method sets the name of the resource to be updated.
     *
     * @param name The name of the resource to be updated.
     */
    void withName(String name);
}
