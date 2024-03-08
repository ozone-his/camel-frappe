package com.ozonehis.camel.frappe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ozonehis.camel.AbstractFrappeTestSupport;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappeDeleteApiMethod;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

@Slf4j
public class DeleteResourceIntegrationTest extends AbstractFrappeTestSupport {

    private static final String CUSTOMER_DOCTYPE = "Customer";

    private static final String DELETE_PATH_PREFIX = FrappeApiCollection.getCollection()
            .getApiName(FrappeDeleteApiMethod.class)
            .getName();

    private static final String CUSTOMER_JSON = "{\"customer_name\":\"John\",\"customer_type\":\"Company\"}";

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // Get a customer route
                from("direct://getCustomer")
                        .toD("frappe://get/resource?doctype=" + CUSTOMER_DOCTYPE)
                        .end();

                // Create a customer route
                from("direct://createCustomer")
                        .toD("frappe://post/resource?doctype=" + CUSTOMER_DOCTYPE)
                        .end();

                // Delete a customer route
                from("direct://deleteCustomer")
                        .toD("frappe://" + DELETE_PATH_PREFIX + "/resource?doctype=" + CUSTOMER_DOCTYPE)
                        .end();
            }
        };
    }

    @Test
    public void shouldDeleteResourceWithCustomerDocType() {
        // Create a customer
        var postHeaders = new HashMap<String, Object>();
        postHeaders.put("CamelFrappe.doctype", CUSTOMER_DOCTYPE);
        postHeaders.put("CamelFrappe.resource", CUSTOMER_JSON);

        var response = requestBodyAndHeaders("direct://createCustomer", CUSTOMER_JSON, postHeaders, String.class);
        assertNotNull(response, "Customer is created");

        // Get just created customer named John
        var getHeaders = new HashMap<String, Object>();
        getHeaders.put("CamelFrappe.filters", List.of(List.of("customer_name", "=", "John")));
        getHeaders.put("CamelFrappe.fields", List.of("customer_name", "customer_type", "name"));

        var customer = requestBodyAndHeaders("direct://getCustomer", null, getHeaders, String.class);

        assertNotNull(customer);
        assertTrue(customer.contains("John"));

        // Delete the customer
        var deleteHeaders = new HashMap<String, Object>();
        deleteHeaders.put("CamelFrappe.name", "John");

        requestBodyAndHeaders("direct://deleteCustomer", null, deleteHeaders, String.class);

        // Get the customer again
        var customerAfterDelete = requestBodyAndHeaders("direct://getCustomer", null, getHeaders, String.class);

        assertNotNull(customerAfterDelete);
        assertEquals("{\"data\":[]}", customerAfterDelete, "Customer is deleted");
    }
}
