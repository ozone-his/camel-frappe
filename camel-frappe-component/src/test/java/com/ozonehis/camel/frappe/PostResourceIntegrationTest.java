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
	
	private static final String POST_PATH_PREFIX = FrappeApiCollection.getCollection().getApiName(FrappePostApiMethod.class)
			.getName();
	
	private static final String CUSTOMER_DOCTYPE = "Customer";
	
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			
			@Override
			public void configure() {
				from("direct://createCustomer")
						.setProperty("resource", simple("${body}"))
						.toD("frappe://" + POST_PATH_PREFIX + "/resource?doctype=" + CUSTOMER_DOCTYPE)
						.end();
				
				from("direct://getCustomer")
						.to("frappe://get/resource?doctype=" + CUSTOMER_DOCTYPE)
						.log("Customer: ${body}").end();
			}
		};
	}
	
	@Test
	public void shouldCreateResourceWithCustomerDocType() {
		var postHeaders = new HashMap<String, Object>();
		postHeaders.put("CamelFrappe.doctype", CUSTOMER_DOCTYPE);
		postHeaders.put("CamelFrappe.resource", CUSTOMER_JSON);
		
		var customer = requestBodyAndHeaders("direct://createCustomer", CUSTOMER_JSON, postHeaders, String.class);
		
		assertNotNull(customer, "returns Customer");
		assertTrue(customer.contains("John"), "Customer Name is John");
		
		// Get just created customer named John
		var headers = new HashMap<String, Object>();
		headers.put("CamelFrappe.filters", List.of(List.of("customer_name", "=", "John")));
		var customers = requestBodyAndHeaders("direct://getCustomer", null, headers, String.class);
		
		assertNotNull(customers, "Get Customer Doctype");
		assertTrue(customers.contains("John"), "Returns John");
	}
	
	private static final String CUSTOMER_JSON = "{\"customer_name\":\"John\",\"customer_type\":\"Company\"}";
}
