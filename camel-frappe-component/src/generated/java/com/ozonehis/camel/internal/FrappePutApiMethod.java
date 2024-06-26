/*
 * Camel ApiMethod Enumeration generated by camel-api-component-maven-plugin
 */
package com.ozonehis.camel.internal;

import java.lang.reflect.Method;
import java.util.List;

import com.ozonehis.camel.api.proxy.FrappePut;

import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodArg;
import org.apache.camel.support.component.ApiMethodImpl;

import static org.apache.camel.support.component.ApiMethodArg.arg;

/**
 * Camel {@link ApiMethod} Enumeration for com.ozonehis.camel.api.proxy.FrappePut
 */
public enum FrappePutApiMethod implements ApiMethod {

    RESOURCE(
        java.io.InputStream.class,
        "resource",
        arg("doctype", String.class),
        arg("name", String.class),
        arg("resource", Object.class));

    private final ApiMethod apiMethod;

    private FrappePutApiMethod(Class<?> resultType, String name, ApiMethodArg... args) {
        this.apiMethod = new ApiMethodImpl(FrappePut.class, resultType, name, args);
    }

    @Override
    public String getName() { return apiMethod.getName(); }

    @Override
    public Class<?> getResultType() { return apiMethod.getResultType(); }

    @Override
    public List<String> getArgNames() { return apiMethod.getArgNames(); }

    @Override
    public List<Class<?>> getArgTypes() { return apiMethod.getArgTypes(); }

    @Override
    public Method getMethod() { return apiMethod.getMethod(); }
}
