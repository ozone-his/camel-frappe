package com.ozonehis.camel.frappe.sdk.api;

import lombok.Getter;

@Getter
public class RemoteFrappeClientException extends FrappeClientException {

    private final int httpStatusCode;

    private final String body;

    public RemoteFrappeClientException(String message, int httpStatusCode, String body) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.body = body;
    }

    @Override
    public String toString() {
        String string = super.toString();
        return (body != null) ? (string + " - " + body) : string;
    }
}
