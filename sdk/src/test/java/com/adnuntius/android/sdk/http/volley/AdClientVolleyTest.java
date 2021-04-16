package com.adnuntius.android.sdk.http.volley;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.ad.AdClient;
import com.adnuntius.android.sdk.ad.AdResponseHandler;
import com.adnuntius.android.sdk.http.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class AdClientVolleyTest {
    private final Gson gson = new GsonBuilder().create();
    private HttpClient httpClient;
    private TestHttpRequestQueue queue;
    private AdClient adClient;

    @Before
    public void setUp() {
        queue = new TestHttpRequestQueue();
        httpClient = new VolleyHttpClient(queue);
        adClient = new AdClient(AdnuntiusEnvironment.dev, httpClient);
    }

    @Test
    public void testGetJsonAd() {
        final JsonObject request = gson.fromJson("{\"adUnits\": [{\"auId\": \"00000000000004a3\"}]}", JsonObject.class);
        final AdResponseHandler handler = mock(AdResponseHandler.class);
        ArgumentCaptor<JsonObject> responseCapture = ArgumentCaptor.forClass(JsonObject.class);
        doNothing().when(handler).onSuccess(responseCapture.capture());

        adClient.request(request, handler);
        queue.waitForMessages();
        final JsonObject response = responseCapture.getValue();
        assertNotNull(response);
    }
}
