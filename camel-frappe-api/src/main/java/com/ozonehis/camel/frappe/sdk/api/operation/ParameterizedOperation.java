package com.ozonehis.camel.frappe.sdk.api.operation;

import java.util.Map;

/**
 * This interface represents a parameterized operation.
 *
 * @param <T> The type of the result of the operation.
 */
public interface ParameterizedOperation<T> {
	
	/**
	 * This method sets the parameters to be used in the operation.
	 *
	 * @param parameters The parameters to be used in the operation.
	 * @return The ParameterizedOperation instance.
	 */
	ParameterizedOperation<T> withParameters(Map<String, String> parameters);
	
	/**
	 * This method sets the parameter to be used in the operation.
	 *
	 * @param name The name of the parameter.
	 * @param value The value of the parameter.
	 * @return The ParameterizedOperation instance.
	 */
	ParameterizedOperation<T> withParameter(String name, String value);
	
	/**
	 * This method executes the operation.
	 *
	 * @return The result of the operation.
	 */
	T execute();
	
}
