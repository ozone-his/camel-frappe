package com.ozonehis.camel.frappe;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ozonehis.camel.AbstractFrappeTestSupport;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappePutApiMethod;
import java.util.HashMap;
import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class PutResourceIntegrationTest extends AbstractFrappeTestSupport {

    private static final String PUT_PATH_PREFIX = FrappeApiCollection.getCollection()
            .getApiName(FrappePutApiMethod.class)
            .getName();

    private static final String DOCTYPE = "Customer";

    private static final String CUSTOMER_JSON = "{\"customer_name\":\"John\",\"customer_type\":\"Company\"}";

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct://updateCustomer")
                        .setProperty("resource", simple("${body}"))
                        .toD("frappe://" + PUT_PATH_PREFIX + "/resource?doctype=" + DOCTYPE)
                        .end();

                from("direct://getCustomer")
                        .to("frappe://get/resource?doctype=" + DOCTYPE)
                        .log("Customer: ${body}")
                        .end();

                from("direct://createCustomer")
                        .setProperty("resource", simple("${body}"))
                        .toD("frappe://post/resource?doctype=" + DOCTYPE)
                        .end();
            }
        };
    }

    @Test
    public void shouldUpdateResourceWithCustomerDocType() {
        // Create a customer
        var postHeaders = new HashMap<String, Object>();
        postHeaders.put("CamelFrappe.doctype", DOCTYPE);
        postHeaders.put("CamelFrappe.resource", CUSTOMER_JSON);

        requestBodyAndHeaders("direct://createCustomer", CUSTOMER_JSON, postHeaders, String.class);

        // Get just created customer named John
        var getHeaders = new HashMap<String, Object>();
        getHeaders.put("CamelFrappe.filters", List.of(List.of("customer_name", "=", "John")));

        var customer = requestBodyAndHeaders("direct://getCustomer", null, getHeaders, String.class);

        assertNotNull(customer, "Returns customer named John");
        assertTrue(customer.contains("John"), "Returns John");

        // Update the customer
        var putHeaders = new HashMap<String, Object>();
        putHeaders.put("CamelFrappe.doctype", DOCTYPE);
        putHeaders.put("CamelFrappe.name", "John");
        putHeaders.put("CamelFrappe.resource", "{\"customer_name\":\"John\",\"customer_type\":\"Individual\"}");

        var updatedCustomer = requestBodyAndHeaders("direct://updateCustomer", null, putHeaders, String.class);

        assertNotNull(updatedCustomer, "returns updated customer");
        assertTrue(updatedCustomer.contains("Individual"), "Customer type is Individual");
    }
}
