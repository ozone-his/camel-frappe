package com.ozonehis.camel.frappe.sdk.internal.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozonehis.camel.frappe.sdk.api.FrappeClientException;
import com.ozonehis.camel.frappe.sdk.api.transformer.Transformer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class JacksonTransformer implements Transformer {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String transform(InputStream from) {
		try {
			return objectMapper.readValue(from, String.class);
		}
		catch (IOException e) {
			throw new FrappeClientException(e);
		}
	}
	
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
	public <T> T transform(InputStream from, Class<T> type) {
		try {
			return objectMapper.readValue(from, type);
		}
		catch (IOException e) {
			throw new FrappeClientException(e);
		}
	}
	
	@Override
	public <T> T transform(InputStream from, TypeReference<T> toType) {
		try {
			return objectMapper.readValue(from, toType);
		}
		catch (IOException e) {
			throw new FrappeClientException(e);
		}
	}
}
