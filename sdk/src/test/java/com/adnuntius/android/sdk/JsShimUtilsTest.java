package com.adnuntius.android.sdk;

import org.junit.Assert;
import org.junit.Test;

public class JsShimUtilsTest extends Assert {
    @Test
    public void testInsertShimNoHead() {
        String script = "<html some-attribute some-other-attribute><body>im a body</body></html>";
        String result = JsShimUtils.injectShim(script);
        assertTrue(result.contains("<html some-attribute some-other-attribute>\n<head>\n<script "));
    }

    @Test
    public void testInsertShimInHead() {
        String script = "<html><head><h1>some head</h1></head><body>im a body</body></html>";
        String result = JsShimUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test
    public void testInsertShimInSelfClosingHead() {
        String script = "<html><head/><h1>some head</h1></head><body>im a body</body></html>";
        String result = JsShimUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test
    public void testInsertShimInSelfClosingHeadWithSpace() {
        String script = "<html><head  /><h1>some head</h1></head><body>im a body</body></html>";
        String result = JsShimUtils.injectShim(script);
        assertTrue(result.contains("<html><head>\n<script "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNoHeadNoHtmlIsIllegal() {
        String script = "<xhtml><body>im a body</body></html>";
        JsShimUtils.injectShim(script);
    }
}
