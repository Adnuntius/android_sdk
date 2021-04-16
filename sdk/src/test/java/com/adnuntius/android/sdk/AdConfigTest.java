package com.adnuntius.android.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public class AdConfigTest extends Assert {
    @Test
    public void testToScript() {
        AdConfig cfg = new AdConfig("0000000000023ae5")
                .setHeight(300)
                .setWidth(250)
                .addKeyValue("car", "toyota")
                .addKeyValue("car", "ford")
                .addKeyValue("sport", "football")
                .addCategories("sports", "casinos");

        Gson gson = new Gson();
        final String script = gson.toJson(cfg).replace('"', '\'');

        assertTrue(script.contains("'auId':'0000000000023ae5'"));
        assertTrue(script.contains("'kv':{'car':['toyota','ford']"));
        assertTrue(script.contains(",'sport':['football']"));
        assertTrue(script.contains("'c':['sports','casinos']"));

        final JsonObject jsonObject = gson.fromJson(script, JsonObject.class);
        assertNotNull(jsonObject);
        final JsonArray jsonArray = jsonObject.getAsJsonArray("c");
        assertEquals("sports", jsonArray.get(0).getAsString());
        assertEquals("casinos", jsonArray.get(1).getAsString());

        final String adScript = AdUtils.getAdScript(cfg.getAuId(), script);
        assertTrue(adScript.contains("'auId':'0000000000023ae5'"));
        assertTrue(adScript.contains("id=\"adn-0000000000023ae5\""));
        assertTrue(adScript.contains("'kv':{'car':['toyota','ford']"));
        assertTrue(adScript.contains(",'sport':['football']"));
        assertTrue(adScript.contains("'c':['sports','casinos']"));
    }
}
