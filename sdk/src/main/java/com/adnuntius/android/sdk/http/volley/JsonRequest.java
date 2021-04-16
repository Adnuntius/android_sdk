package com.adnuntius.android.sdk.http.volley;

import androidx.annotation.Nullable;

import com.adnuntius.android.sdk.http.BearerToken;
import com.android.volley.AuthFailureError;
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

    public JsonRequest(
            final int method,
            final String url,
            final String jsonString,
            @Nullable final BearerToken token,
            final VolleyResponseHandler handler) {
        super(method,
                url,
                jsonString,
                handler,
                handler);
        setShouldCache(false);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (token != null) {
            Map<String, String> headerMap = new HashMap<>();
            if (getMethod() != Request.Method.GET) {
                headerMap.put("Content-Type", "application/json");
            }
            headerMap.put("Authorization", "Bearer " + token.getAccessToken());
            return headerMap;
        } else {
            return super.getHeaders();
        }
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
