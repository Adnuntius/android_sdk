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

import java.io.UnsupportedEncodingException;

public class VolleyHttpClient implements HttpClient {
    private final HttpRequestQueue requestQueue;

    public VolleyHttpClient(final Context context) {
        this(RequestQueueSingleton.getInstance(context));
    }

    public VolleyHttpClient(final HttpRequestQueue requestQueue) {
        this.requestQueue = requestQueue;
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

    private void jsonRequest(
            final int method, final String url,
                                @Nullable final String jsonString,
                                @Nullable final BearerToken token,
                                final HttpResponseHandler handler)  {
        final JsonRequest request = new JsonRequest(
                method,
                url,
                jsonString,
                token,
                new VolleyResponseHandler() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        // always a valid json response is returned
                        if (jsonResponse == null || jsonResponse.isEmpty() || jsonResponse.trim().isEmpty()) {
                            handler.onSuccess("{}");
                        } else {
                            handler.onSuccess(jsonResponse);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            final String responseData = VolleyHttpClient.toString(error.networkResponse);

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
                });
        requestQueue.add(request);
    }

    private static String toString(final NetworkResponse response) {
        try {
            return new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
