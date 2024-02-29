package com.ozonehis.camel.frappe;

import com.ozonehis.camel.AbstractFrappeTestSupport;
import com.ozonehis.camel.internal.FrappeApiCollection;
import com.ozonehis.camel.internal.FrappePostApiMethod;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostResourceIntegrationTest extends AbstractFrappeTestSupport {
	
	private static final String PATH_PREFIX = FrappeApiCollection.getCollection().getApiName(FrappePostApiMethod.class)
			.getName();
	
	private static final String CUSTOMER_DOCTYPE = "Customer";
	
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			
			@Override
			public void configure() {
				from("direct://create-customer")
						.setProperty("resource", simple("${body}"))
						.toD("frappe://" + PATH_PREFIX + "/resource?doctype=" + CUSTOMER_DOCTYPE + "&resource=" + "${exchangeProperty.resource}")
						.end();
				
				from("direct://get-customer")
						.to("frappe://get/resource?doctype=" + CUSTOMER_DOCTYPE)
						.log("Customer: ${body}").end();
			}
		};
	}
	
	@Test
	public void shouldPostResourceWithAnyDocType() {
		var customer = requestBody("direct://create-customer", CUSTOMER_JSON, String.class);
		
		assertNotNull(customer, "returns Customer");
		assertTrue(customer.contains("John"), "Customer Name is John");
		
		// Get just created customer named John
		var headers = new HashMap<String, Object>();
		headers.put("CamelFrappe.filters", List.of(List.of("customer_name", "=", "John")));
		var customers = requestBodyAndHeaders("direct://get-customer", null, headers, String.class);
		
		assertNotNull(customers, "Get Customer Doctype");
		assertTrue(customers.contains("John"), "Returns John");
	}
	
	private static final String CUSTOMER_JSON = "{\"customer_name\":\"John\",\"customer_type\":\"Company\"}";
}
