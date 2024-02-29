package com.ozonehis.camel.frappe.sdk.internal.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public record JacksonTransformer(ObjectMapper objectMapper) implements Transformer {
	
	@Override
	public String transform(Object from) {
		try {
			return objectMapper.writeValueAsString(from);
		}
		catch (JsonProcessingException e) {
			throw new FrappeClientException(e);
		}
	}
	
	@Override
	public <T> T transform(Object from, Class<T> toType) {
		return objectMapper.convertValue(from, toType);
	}
	
	@Override
	public <T> T transform(Reader source, Class<T> sourceType) {
		try {
			return objectMapper.readValue(source, sourceType);
		}
		catch (IOException e) {
			throw new FrappeClientException(e);
		}
	}
	
	@Override
	public <T> T transform(List<?> from, Class<T> toCollectionType, Class<?> toElementType) {
		return objectMapper.convertValue(from,
				objectMapper.getTypeFactory().constructCollectionLikeType(toCollectionType, toElementType));
	}
	
}
