package com.ozonehis.camel.frappe.sdk.internal.operation;

import com.ozonehis.camel.frappe.sdk.api.FrappeResponse;
import com.ozonehis.camel.frappe.sdk.api.operation.ResourceOperation;
import com.ozonehis.camel.frappe.sdk.api.transformer.TransformerFactory;
import com.ozonehis.camel.frappe.sdk.internal.DefaultFrappeResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DefaultPutOperation extends AbstractResourceOperation  implements ResourceOperation {
	
	public DefaultPutOperation(String baseApiUrl, String path, OkHttpClient httpClient,
			TransformerFactory transformerFactory, String... pathParams) {
		super(baseApiUrl, path, httpClient, transformerFactory, pathParams);
	}
	
	@Override
	protected FrappeResponse doResourceExecute(byte[] resourceAsBytes, Request.Builder requestBuilder) {
		Request request = requestBuilder.post(RequestBody.create(resourceAsBytes)).build();
		okhttp3.Response response = onHttpResponse(() -> httpClient.newCall(request).execute());
		return new DefaultFrappeResponse(response, transformerFactory);
	}
}
