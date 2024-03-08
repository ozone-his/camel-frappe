package com.ozonehis.camel.frappe.sdk.api.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import java.util.List;

/**
 * This interface represents a GET operation.
 */
public interface GetOperation extends ParameterizedOperation<FrappeResponse> {

    /**
     * This method sets the fields to be retrieved.
     *
     * @param fields The fields to be retrieved.
     * @return The GetOperation instance.
     */
    GetOperation withFields(List<String> fields);

    /**
     * This method sets the field to be retrieved.
     *
     * @param field The field to be retrieved.
     * @return The GetOperation instance.
     */
    GetOperation withField(String field);

    /**
     * This method sets the filters to be applied.
     *
     * @param filters The filters to be applied.
     * @return The GetOperation instance.
     */
    GetOperation withFilters(List<List<String>> filters);

    /**
     * This method sets the filter to be applied.
     *
     * @param filter The filter to be applied.
     * @return The GetOperation instance.
     */
    GetOperation withFilter(List<String> filter);
}
