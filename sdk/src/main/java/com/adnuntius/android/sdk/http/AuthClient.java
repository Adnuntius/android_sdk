/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A standalone auth client to authenticate against the adnuntius api to generate and refresh bearer tokens for
 * adnuntius data for querying profile information.
 *
 * Its unlikely this would be used in an android application, but its useful for testing
 */
public class AuthClient {
    private final Gson gson;
    private final AdnuntiusEnvironment env;
    private final HttpClient httpClient;

    /**
     * Production endpoint
     */
    public AuthClient(final HttpClient httpClient) {
        this(AdnuntiusEnvironment.production, httpClient);
    }

    public AuthClient(final AdnuntiusEnvironment env, final HttpClient httpClient) {
        gson = new GsonBuilder().create();
        this.httpClient = httpClient;
        this.env = env;
    }

    public void refresh(final BearerToken token, final AuthenticateResponseHandler handler) {
        final Refresh refresh = new Refresh(token.getRefreshToken());
        doAuth(refresh, handler);
    }

    public void authenticate(final String username, final String password, final AuthenticateResponseHandler handler) {
        final Authenticate auth = new Authenticate(username, password);
        doAuth(auth, handler);
    }

    private void doAuth(final Object auth, final AuthenticateResponseHandler handler) {
        httpClient.postJsonRequest(
                HttpUtils.getAuthUrl(env),
                gson.toJson(auth),
                new HttpResponseHandler() {
                    @Override
                    public void onFailure(final ErrorResponse response) {
                        handler.onFailure(response);
                    }

                    @Override
                    public void onSuccess(final String jsonResponse) {
                        final BearerToken token = gson.fromJson(jsonResponse, BearerToken.class);
                        handler.onSuccess(token);
                    }
                });
    }
}
