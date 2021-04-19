package com.adnuntius.android.sdk;

import com.adnuntius.android.sdk.ad.AdUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public class AdUtilsTest extends Assert {
    @Test
    public void testInsertShimNoHead() {
        String script = "<html some-attribute some-other-attribute><body>im a body</body></html>";
        String result = AdUtils.injectShim(script);
        assertTrue(result.contains("<html some-attribute some-other-attribute>\n<head>\n<script "));
    }

    @Test
    public void testInsertShimInHead() {
        String script = "<html><head><h1>some head</h1></head><body>im a body</body></html>";
        String result = AdUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test
    public void testInsertShimInSelfClosingHead() {
        String script = "<html><head/><h1>some head</h1></head><body>im a body</body></html>";
        String result = AdUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test
    public void testInsertShimInSelfClosingHeadWithSpace() {
        String script = "<html><head  /><h1>some head</h1></head><body>im a body</body></html>";
        String result = AdUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNoHeadNoHtmlIsIllegal() {
        String script = "<xhtml><body>im a body</body></html>";
        AdUtils.injectShim(script);
    }

    @Test
    public void testParseAdDeliveryResponse() throws Exception {
        final String json = ResourceUtils.getResourceAsString("ad-response.json");
        final Gson gson = new GsonBuilder().create();
        final JsonObject response = gson.fromJson(json, JsonObject.class);
        AdUtils.AdResponse adScript = AdUtils.getAdFromDeliveryResponse(response);
        assertNotNull(adScript);
    }

    @Test
    public void testParseNoAdDeliveryResponse() throws Exception {
        final String json = ResourceUtils.getResourceAsString("no-ad-response.json");
        final Gson gson = new GsonBuilder().create();
        final JsonObject response = gson.fromJson(json, JsonObject.class);
        assertNull(AdUtils.getAdFromDeliveryResponse(response));
    }
}
