package com.adnuntius.android.sdk;

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
                .addCategory("sports")
                .addCategory("casinos");

        String script = cfg.toScript();
        //System.out.println(script);

        assertTrue(script.contains("id=\"adn-0000000000023ae5\""));
        assertTrue(script.contains("'kv':{'car':['toyota','ford']"));
        assertTrue(script.contains(",'sport':['football']"));
        assertTrue(script.contains("'c':['sports','casinos']"));
    }
}
