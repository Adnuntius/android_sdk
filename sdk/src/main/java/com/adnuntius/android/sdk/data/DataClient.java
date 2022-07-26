/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data;

import android.content.Context;
import android.icu.util.TimeZone;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;

import com.adnuntius.android.sdk.data.profile.Profile;
import com.adnuntius.android.sdk.data.profile.ProfileResponseHandler;
import com.adnuntius.android.sdk.http.BearerToken;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpClient;
import com.adnuntius.android.sdk.http.HttpUtils;
import com.adnuntius.android.sdk.http.volley.VolleyHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class DataClient {
    private final Gson gson;
    private final AdnuntiusEnvironment env;
    private final HttpClient httpClient;

    /**
     * Production endpoint
     */
    public DataClient(final Context context) {
        this(AdnuntiusEnvironment.production, new VolleyHttpClient(context));
    }

    public DataClient(final AdnuntiusEnvironment env, final HttpClient httpClient) {
        gson = new GsonBuilder().create();
        this.httpClient = httpClient;
        this.env = env;
    }

    /**
     * Refer to https://docs.adnuntius.com/adnuntius-data/api-documentation/http/sync
     *
     * @param sync
     * @param handler
     */
    public void sync(final Sync sync, final DataResponseHandler handler) {
        validateDataRequest(sync);

        if (sync.getExternalSystemType() == null || sync.getExternalSystemUserId() == null) {
            throw new IllegalArgumentException("Need an external system type and external system user id");
        }
        submit(sync, handler);
    }

    /**
     * Refer to https://docs.adnuntius.com/adnuntius-data/api-documentation/http/http-page-view
     *
     * @param page
     * @param handler
     */
    public void page(final Page page, final DataResponseHandler handler) {
        validateDataRequest(page);
        submit(page, handler);
    }

    /**
     * Refer to https://docs.adnuntius.com/adnuntius-data/api-documentation/http/http-profile
     *
     * @param profile
     * @param handler
     */
    public void profile(final Profile profile, final DataResponseHandler handler) {
        validateDataRequest(profile);
        submit(profile, handler);
    }

    public void getProfile(final String folderId, final String browserId, BearerToken token, final ProfileResponseHandler handler) {
        final String url = HttpUtils.getDataUrl(env, DataTarget.visitor, true) + "&browserId=" + browserId + "&folderId=" + folderId;
        httpClient.getJsonRequest(
                url,
                token,
                new HttpResponseHandler() {
                    @Override
                    public void onFailure(final ErrorResponse response) {
                        handler.onFailure(response);
                    }

                    @Override
                    public void onSuccess(final String jsonResponse) {
                        final Profile profile = gson.fromJson(jsonResponse, Profile.class);
                        handler.onSuccess(profile);
                    }
                });
    }

    private void validateDataRequest(DataRequest dataRequest) {
        if (dataRequest.getBrowserId() == null || dataRequest.getFolderId() == null) {
            throw new IllegalArgumentException("Both BrowserId and FolderId are required");
        }
    }

    private void submit(DataRequest request, final DataResponseHandler handler) {
        final JsonObject jsonObject = (JsonObject) gson.toJsonTree(request);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            final TimeZone tz = TimeZone.getDefault();
            int offsetFromUtc = tz.getRawOffset() / (1000 * 60);
            jsonObject.add("userTimezone", new JsonPrimitive(offsetFromUtc + ""));
        }
        jsonObject.add("occurredAt", new JsonPrimitive(System.currentTimeMillis()));

        httpClient.postJsonRequest(
                HttpUtils.getDataUrl(env, request.getTarget(), false),
                gson.toJson(jsonObject),
                new HttpResponseHandler() {
                    @Override
                    public void onFailure(final ErrorResponse response) {
                        handler.onFailure(response);
                    }

                    @Override
                    public void onSuccess(final String jsonResponse) {
                        handler.onSuccess();
                    }
                });
    }
}
