package com.ozonehis.camel;

import com.ozonehis.camel.frappe.sdk.FrappeClientBuilder;
import org.apache.camel.RuntimeCamelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FrappeComponentTest {
	
	@Test
	public void shouldThrowExceptionGivenConfigurationWithFrappeClientAndBaseApiUrl() {
		FrappeConfiguration configuration = new FrappeConfiguration();
		configuration.setFrappeClient(FrappeClientBuilder.newClient("http://localhost:8080", "Administrator", "admin").build());
		configuration.setBaseApiUrl("http://localhost:8080");
		
		FrappeComponent frappeComponent = new FrappeComponent();
		frappeComponent.setConfiguration(configuration);
		
		assertThrows(RuntimeCamelException.class, () -> frappeComponent.getClient(configuration));
	}
	
}
