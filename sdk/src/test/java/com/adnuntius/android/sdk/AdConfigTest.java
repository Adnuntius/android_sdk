package com.adnuntius.android.sdk;

import static com.adnuntius.android.sdk.AdnuntiusEnvironment.andemu;
import static com.adnuntius.android.sdk.AdnuntiusEnvironment.production;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public class AdConfigTest extends Assert {
    private final Gson gson = new Gson();

    @Test
    public void loadFromApiToAdRequest() {
        final String json = "{\"adUnits\": [{\"auId\": \"000000000006f450\", \"c\": [\"something\", \"else\"], \"auW\": \"121\", \"auH\": \"333\", \"kv\": {\"version\":[\"10\"]}}]}";
        final AdRequests adRequests = gson.fromJson(json, AdRequests.class);
        assertEquals("000000000006f450", adRequests.getAdUnits().get(0).auId());
        assertEquals("10", adRequests.getAdUnits().get(0).getKeyValues().get("version").get(0));
        assertEquals("121", adRequests.getAdUnits().get(0).getWidth());
        assertEquals("333", adRequests.getAdUnits().get(0).getHeight());
        assertEquals("something", adRequests.getAdUnits().get(0).getCategories().get(0));
        assertEquals("else", adRequests.getAdUnits().get(0).getCategories().get(1));
    }

    @Test
    public void testToScript() {
        AdRequest cfg = new AdRequest("0000000000023ae5")
                .setHeight(300)
                .setWidth(250)
                .addKeyValue("car", "toyota")
                .addKeyValue("car", "ford")
                .addKeyValue("sport", "football")
                .useCookies(false)
                .addCategories("sports", "casinos");

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

        final String adScript = AdUtils.getAdScript(production, cfg, script, false);
        assertTrue(adScript.contains("useCookies: false,"));
        assertFalse(adScript.contains("userId: "));
        assertFalse(adScript.contains("consentString: "));
        assertTrue(adScript.contains("src=\"" + AdUtils.getAdnJsUrl(production) + "\""));
        assertFalse(adScript.contains("sessionId: "));
        assertFalse(adScript.contains("'useCookies':false"));
        assertTrue(adScript.contains("'auId':'0000000000023ae5'"));
        assertTrue(adScript.contains("id=\"adn-0000000000023ae5\""));
        assertTrue(adScript.contains("'kv':{'car':['toyota','ford']"));
        assertTrue(adScript.contains(",'sport':['football']"));
        assertTrue(adScript.contains("'c':['sports','casinos']"));

        cfg.useCookies(true)
                .userId("my user id")
                .sessionId("my session id")
                .consentString("my consent string")
                .globalParameter("gdpr", "1");

        final String adScript2 = AdUtils.getAdScript(andemu, cfg, script, false);
        assertFalse(adScript2.contains("useCookies: false,"));
        assertTrue(adScript2.contains("userId: \"my user id\","));
        assertTrue(adScript2.contains("userId: \"my user id\","));
        assertTrue(adScript2.contains("gdpr: \"1\","));
        assertTrue(adScript2.contains("consentString: \"my consent string\","));
        assertTrue(adScript2.contains("src=\"" + AdUtils.getAdnJsUrl(andemu) + "\""));
        assertFalse(adScript2.contains("'useCookies':false"));
        assertTrue(adScript2.contains("'auId':'0000000000023ae5'"));
        assertTrue(adScript2.contains("id=\"adn-0000000000023ae5\""));
        assertTrue(adScript2.contains("'kv':{'car':['toyota','ford']"));
        assertTrue(adScript2.contains(",'sport':['football']"));
        assertTrue(adScript2.contains("'c':['sports','casinos']"));
    }
}
