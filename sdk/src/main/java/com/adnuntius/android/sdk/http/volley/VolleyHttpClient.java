package com.adnuntius.android.sdk.http.volley;

import android.content.Context;

import androidx.annotation.Nullable;

import com.adnuntius.android.sdk.http.BearerToken;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpClient;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class VolleyHttpClient implements HttpClient {
    private final HttpRequestQueue requestQueue;
    private String userAgent;

    public VolleyHttpClient(final Context context) {
        this(RequestQueueSingleton.getInstance(context));
    }

    public VolleyHttpClient(final HttpRequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public void getRequest(final String url, final HttpResponseHandler handler) {
        httpRequest(Request.Method.GET, url, handler);
    }
    @Override
    public void getJsonRequest(final String url, final HttpResponseHandler handler) {
        jsonRequest(Request.Method.GET, url, null, null, handler);
    }

    @Override
    public void getJsonRequest(final String url, final BearerToken token,
                               final HttpResponseHandler handler) {
        if (token == null) {
            throw new IllegalArgumentException("Token is required");
        }
        jsonRequest(Request.Method.GET, url, null, token, handler);
    }

    @Override
    public void postJsonRequest(final String url,
                                final String jsonString,
                                final HttpResponseHandler handler)  {
        jsonRequest(Request.Method.POST, url, jsonString, null, handler);
    }

    @Override
    public void postJsonRequest(final String url,
                                final String jsonString,
                                final BearerToken token,
                                final HttpResponseHandler handler)  {
        if (token == null) {
            throw new IllegalArgumentException("Token is required");
        }
        jsonRequest(Request.Method.POST, url, jsonString, token, handler);
    }

    private void httpRequest(final int method, final String url, final HttpResponseHandler handler) {
        final VolleyResponseHandler volleyHandler = new DefaultVolleyResponseHandler(handler, false);
        final StringRequest request = new StringRequest(
                method,
                url,
                volleyHandler,
                volleyHandler);
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void jsonRequest(
            final int method,
            final String url,
            @Nullable final String jsonString,
            @Nullable final BearerToken token,
            final HttpResponseHandler handler)  {
        final JsonRequest request = new JsonRequest(
                method,
                url,
                jsonString,
                userAgent,
                token,
                new DefaultVolleyResponseHandler(handler, true));
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
