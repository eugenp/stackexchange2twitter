package org.stackexchange.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;

public class RequestBuilder {

    private Map<String, Object> parameters;

    public RequestBuilder() {
        this.parameters = new HashMap<>();
    }

    // API

    public final RequestBuilder add(final String paramName, final Object paramValue) {
        this.parameters.put(paramName, paramValue);
        return this;
    }

    public final String build() {
        final URIBuilder uriBuilder = new URIBuilder();
        for (final Entry<String, Object> param : this.parameters.entrySet()) {
            uriBuilder.addParameter(param.getKey(), param.getValue().toString());
        }

        return uriBuilder.toString();
    }

}
