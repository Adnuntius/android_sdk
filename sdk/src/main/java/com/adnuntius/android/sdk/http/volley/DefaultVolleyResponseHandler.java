/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class DefaultVolleyResponseHandler implements VolleyResponseHandler {
    private boolean isJson;
    private final HttpResponseHandler handler;

    public DefaultVolleyResponseHandler(final HttpResponseHandler handler, final boolean isJson) {
        this.handler = handler;
        this.isJson = isJson;
    }

    @Override
    public void onErrorResponse(final VolleyError error) {
        if (error.networkResponse != null) {
            final String responseData = toString(error.networkResponse);

            handler.onFailure(new ErrorResponse(error.networkResponse.statusCode,
                    responseData + ": " + error.getMessage()));
        } else if (error.getCause() instanceof NullPointerException) {
            // this only occurs when using robolectric as a normal test, when executing on an
            // emulator there is no issue at all, it correctly returns the 404 error
            handler.onFailure(new ErrorResponse(404, ""));
        } else { // a bad thing has happened, and volley should have logged it already
            handler.onFailure(new ErrorResponse(-1, error.getMessage()));
        }
    }

    private static String toString(final NetworkResponse response) {
        try {
            return new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public void onResponse(final String response) {
        // always a valid json response is returned
        if (response == null || response.isEmpty() || response.trim().isEmpty()) {
            handler.onSuccess(isJson ? "{}" : "");
        } else {
            handler.onSuccess(response);
        }
    }
}
