/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import com.adnuntius.android.sdk.http.HttpClient;

import org.junit.Before;
import org.junit.Test;

public class VolleyHttpClientGetRequestTest {
    private static final String TAG = "Test";

    private HttpClient httpClient;
    private final MockResponseHandler handler = new MockResponseHandler();

    @Before
    public void setUp() {
        httpClient = new VolleyHttpClient((Context) getApplicationContext());
        handler.clear();
    }

    @Test
    public void testQueryAdnJsOnIpAddress() {
        final String url = "http://10.0.2.2:8001/adn.src.js?script-override=andemu";
        Log.i(TAG, "The url is " + url);
        httpClient.getRequest(url, handler);
        handler.waitForMessages(1);
        assertEquals(1, handler.responses.size());
        Log.i(TAG, handler.responses.get(0));
    }
}
