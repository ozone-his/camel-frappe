package com.ozonehis.camel.frappe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ozonehis.camel.AbstractFrappeTestSupport;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappeGetApiMethod;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

@Slf4j
public class GetResourceIntegrationTest extends AbstractFrappeTestSupport {

    private static final String PATH_PREFIX = FrappeApiCollection.getCollection()
            .getApiName(FrappeGetApiMethod.class)
            .getName();

    private static final String CUSTOMER_DOCTYPE = "Customer";

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct://getCustomer")
                        .to("frappe://" + PATH_PREFIX + "/resource?doctype=" + CUSTOMER_DOCTYPE)
                        .log("Customer: ${body}");
            }
        };
    }

    @Test
    public void shouldGetResourceWithAnyDocType() {
        var customer = requestBodyAndHeaders("direct://getCustomer", null, new HashMap<>(), String.class);

        assertNotNull(customer, "Get Customer Doctype");
        assertEquals("{\"data\":[]}", customer, "Customer is an Empty list");
    }

    @Test
    public void shouldGetResourceWithAnyDocTypeAndFields() {
        var headers = new HashMap<String, Object>();
        var fields =
                List.of("customer_name", "customer_type", "territory", "customer_group", "customer_primary_contact");
        headers.put("CamelFrappe.fields", fields);

        var customer = requestBodyAndHeaders("direct://getCustomer", null, headers, String.class);

        assertNotNull(customer, "Get Customer Doctype");
        assertEquals("{\"data\":[]}", customer, "Returns an Empty list");
    }

    @Test
    public void shouldGetResourceWithAnyDocTypeAndFilters() {
        var headers = new HashMap<String, Object>();
        var filters = List.of(List.of("customer_name", "=", "John"));
        headers.put("CamelFrappe.filters", filters);

        var customer = requestBodyAndHeaders("direct://getCustomer", null, headers, String.class);

        assertNotNull(customer, "Get Customer Doctype");
        assertEquals("{\"data\":[]}", customer, "Returns an Empty list");
    }
}
