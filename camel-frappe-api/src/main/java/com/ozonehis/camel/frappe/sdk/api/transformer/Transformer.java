package com.ozonehis.camel.frappe.sdk.api.transformer;

import java.io.Reader;
import java.util.List;

/**
 * This interface represents a transformer.
 */
public interface Transformer {
	
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
	 * @param from The object to be transformed.
	 * @param toType The type of the transformed object.
	 * @return The transformed object.
	 */
	<T> T transform( Object from, Class<T> toType );
	
	/**
	 * This method transforms the reader.
	 *
	 * @param source The source reader.
	 * @param sourceType The type of the source reader.
	 * @return The transformed object.
	 */
	<T> T transform( Reader source, Class<T> sourceType);
	
	/**
	 * This method transforms the list.
	 *
	 * @param from The list to be transformed.
	 * @param toCollectionType The type of the transformed list.
	 * @param toElementType The type of the elements of the transformed list.
	 * @return The transformed list.
	 */
	<T> T transform( List<?> from, Class<T> toCollectionType, Class<?> toElementType );
}
