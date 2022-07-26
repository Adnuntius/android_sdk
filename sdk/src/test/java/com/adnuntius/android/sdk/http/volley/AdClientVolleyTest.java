/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.adnuntius.android.sdk.AdRequest;
import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.ad.AdClient;
import com.adnuntius.android.sdk.ad.AdResponse;
import com.adnuntius.android.sdk.ad.AdResponseHandler;
import com.adnuntius.android.sdk.data.DataResponseHandler;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpClient;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AdClientVolleyTest {
    private HttpClient httpClient;
    private TestHttpRequestQueue queue;
    private AdClient devAdClient;
    private AdClient adClient;

    @Before
    public void setUp() {
        queue = new TestHttpRequestQueue();
        httpClient = new VolleyHttpClient(queue);
        //httpClient.setUserAgent("Mozilla/5.0 (Linux; Android 11; Android SDK built for x86 Build/RSR1.210210.001.A1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/83.0.4103.106 Mobile Safari/537.36");
        devAdClient = new AdClient(AdnuntiusEnvironment.dev, httpClient);
        adClient = new AdClient(AdnuntiusEnvironment.production, httpClient);
    }

    @Test
    @Ignore
    public void testGetAd() {
        AdRequest request = new AdRequest("000000000006f450")
                .setWidth(300)
                .setHeight(160)
                .consentString("some consent string")
                .parentParameter("gdpr", "1")
                .addKeyValue("version", "10");

        final AdResponseHandler handler = mock(AdResponseHandler.class);
        adClient.request(request, handler);
        queue.waitForMessages();
        verify(handler).onSuccess(any(AdResponse.class));

        devAdClient.request(request, handler);
        queue.waitForMessages();
        verify(handler).onFailure(any(ErrorResponse.class));

    }
}
