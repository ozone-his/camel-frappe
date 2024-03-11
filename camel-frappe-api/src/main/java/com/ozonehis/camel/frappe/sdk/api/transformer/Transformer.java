package com.ozonehis.camel.frappe.sdk.api.transformer;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;

/**
 * This interface represents a transformer.
 */
public interface Transformer {

    /**
     * This method transforms the object.
     *
     * @param from The InputStream to be transformed.
     * @return The transformed object.
     */
    String transform(InputStream from);

    /**
     * This method transforms the object.
     *
     * @param from The object to be transformed.
     * @return The transformed object.
     */
    String transform(Object from);

    /**
     * This method transforms the object.
     *
     * @param from The InputStream to be transformed.
     * @param toType The type of the transformed object.
     * @return The transformed object.
     */
    <T> T transform(InputStream from, Class<T> toType);

    /**
     * This method transforms the object.
     *
     * @param from The InputStream to be transformed.
     * @param toType The type of the transformed object.
     * @return The transformed object.
     */
    <T> T transform(InputStream from, TypeReference<T> toType);
}
