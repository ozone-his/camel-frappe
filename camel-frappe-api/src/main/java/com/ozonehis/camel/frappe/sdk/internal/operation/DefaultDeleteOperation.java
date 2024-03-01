package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.DeleteOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DefaultDeleteOperation extends AbstractResourceOperation  implements DeleteOperation {
	
	private String nameOfResourceToBeDeleted;
	
	public DefaultDeleteOperation(String baseApiUrl, String path, OkHttpClient httpClient,
			TransformerFactory transformerFactory) {
		super(baseApiUrl, path, httpClient, transformerFactory);
	}
	
	@Override
	protected FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder) {
		Request request = requestBuilder.delete().url(baseApiUrl + path + "/" + nameOfResourceToBeDeleted).build();
		okhttp3.Response response = onHttpResponse(() -> httpClient.newCall(request).execute());
		return new DefaultFrappeResponse(response, transformerFactory);
	}
	
	@Override
	public DeleteOperation withName(String name) {
		this.nameOfResourceToBeDeleted = name;
		return this;
	}
}
