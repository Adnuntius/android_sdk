/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import androidx.annotation.Nullable;

import com.adnuntius.android.sdk.http.BearerToken;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonRequest extends com.android.volley.toolbox.JsonRequest<String> {
    private final BearerToken token;
    private final String userAgent;

    public JsonRequest(
            final int method,
            final String url,
            final String jsonString,
            final String userAgent,
            @Nullable final BearerToken token,
            final VolleyResponseHandler handler) {
        super(method,
                url,
                jsonString,
                handler,
                handler);
        setShouldCache(false);
        this.userAgent = userAgent;
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        if (userAgent != null) {
            headerMap.put("User-agent", userAgent);
        }

        if (token != null) {
            if (getMethod() != Request.Method.GET) {
                headerMap.put("Content-Type", "application/json");
            }
            headerMap.put("Authorization", "Bearer " + token.getAccessToken());
        }
        return headerMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            final String jsonString =
                    new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(
                    jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
