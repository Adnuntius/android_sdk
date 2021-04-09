package com.adnuntius.android.sdk.http;

public interface HttpClient {
    void postJsonRequest(final String url, final String jsonString, final HttpResponseHandler handler);
    void getJsonRequest(final String url, final HttpResponseHandler handler);
    void getJsonRequest(final String url, final BearerToken token, final HttpResponseHandler handler);
    void postJsonRequest(final String url, final String jsonString, final BearerToken token, final HttpResponseHandler handler);
}
