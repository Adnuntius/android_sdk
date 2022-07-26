package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class InternalAdnuntiusJavascriptCallback {
    private final LoadAdHandler handler;
    private final AdnuntiusEnvironment env;
    private final Logger logger;

    public InternalAdnuntiusJavascriptCallback(final AdnuntiusEnvironment env, final LoadAdHandler handler, final Logger logger) {
        this.handler = handler;
        this.env = env;
        this.logger = logger;
    }

    @JavascriptInterface
    public void onComplete(String type, int adCount, int definedWidth, int definedHeight, int width,
                           int height, String creativeId, String lineItemId) {
        logger.debug("IntAdnJSCallback", "onComplete: {0}", type);

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
        logger.debug("IntAdnJSCallback", "onFailure: {0} - {1}", httpStatus, statusMessage);
        this.handler.onFailure(statusMessage + " (" + httpStatus + ") error returned");
    }

    @JavascriptInterface
    public void log(String message) {
        this.logger.debug("console", message);
    }
}
