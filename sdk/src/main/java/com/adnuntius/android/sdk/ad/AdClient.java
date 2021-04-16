package com.adnuntius.android.sdk.ad;

import android.content.Context;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpClient;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpUtils;
import com.adnuntius.android.sdk.http.volley.VolleyHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * https://docs.adnuntius.com/adnuntius-advertising/requesting-ads/http-api
 *
 * Initial implementation of an interface to do json ad requests to adnuntius ad server
 */
public class AdClient {
    private final Gson gson;
    private final AdnuntiusEnvironment env;
    private final HttpClient httpClient;

    /**
     * Production endpoint
     */
    public AdClient(final Context context) {
        this(AdnuntiusEnvironment.production, new VolleyHttpClient(context));
    }

    public AdClient(final AdnuntiusEnvironment env, final HttpClient httpClient) {
        gson = new GsonBuilder().create();
        this.httpClient = httpClient;
        this.env = env;
    }

    public AdnuntiusEnvironment getEnv() {
        return env;
    }

    /**
     * @param requestJson - json string ad request
     * @param handler
     */
    public void request(final String requestJson, final AdResponseHandler handler) {
        final JsonObject request = gson.fromJson(requestJson, JsonObject.class);
        submit(request, handler);
    }

    /**
     * @param request - Gson JsonObject ad request
     * @param handler
     */
    public void request(final JsonObject request, final AdResponseHandler handler) {
        submit(request, handler);
    }

    private void submit(JsonObject request, final AdResponseHandler handler) {
        httpClient.postJsonRequest(
                HttpUtils.getDeliveryUrl(env, DeliveryTarget.Impression),
                gson.toJson(request),
                new HttpResponseHandler() {
                    @Override
                    public void onFailure(final ErrorResponse response) {
                        handler.onFailure(response);
                    }

                    @Override
                    public void onSuccess(final String jsonResponse) {
                        final JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
                        handler.onSuccess(response);
                    }
                });
    }
}
