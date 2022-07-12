package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class InternalAdnuntiusJavascriptCallback {
    private final LoadAdHandler handler;
    private final AdnuntiusEnvironment env;

    public InternalAdnuntiusJavascriptCallback(final AdnuntiusEnvironment env, final LoadAdHandler handler) {
        this.handler = handler;
        this.env = env;
    }

    @JavascriptInterface
    public void onComplete(String type, int adCount, int definedWidth, int definedHeight, int width, int height, String creativeId, String lineItemId) {
        if ("pageLoad".equals(type) && adCount == 0) {
            this.handler.onNoAdResponse();
            return;
        }

        final LoadAdHandler.AdResponseInfo response = new LoadAdHandler.AdResponseInfo();
        response.definedWidth = definedWidth;
        response.definedHeight = definedHeight;
        response.width = width;
        response.height = height;
        response.lineItemId = lineItemId;
        response.creativeId = creativeId;

        if ("pageLoad".equals(type)) {
            this.handler.onAdResponse(response);
        } else { // assumes all other events are resize events
            this.handler.onAdResize(response);
        }
    }

    @JavascriptInterface
    public void onFailure(int httpStatus, String statusMessage) {
        this.handler.onFailure(statusMessage + " (" + httpStatus + ") error returned");
    }
}
