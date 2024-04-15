/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class InternalAdnuntiusJavascriptCallback {
    private final AdnuntiusAdWebView view;
    private final LoadAdHandler handler;
    private final AdnuntiusEnvironment env;
    private final Logger logger;

    public InternalAdnuntiusJavascriptCallback(final AdnuntiusAdWebView view, final AdnuntiusEnvironment env, final LoadAdHandler handler, final Logger logger) {
        this.view = view;
        this.handler = handler;
        this.env = env;
        this.logger = logger;
    }

    @JavascriptInterface
    public void onComplete(final String type, final int adCount, final int width, final int height, final int definedWidth, final int definedHeight, final String creativeId, final String lineItemId) {
        logger.debug("IntAdnJSCallback", "onComplete: {0}", type);

        if ("pageLoad".equals(type) && adCount == 0) {
            this.handler.onNoAdResponse(view);
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
            this.handler.onAdResponse(view, response);
        } else { // assumes all other events are resize events
            this.handler.onAdResize(view, response);
        }
    }

    @JavascriptInterface
    public void onNoMatchedAds() {
        this.handler.onNoAdResponse(view);
    }

    @JavascriptInterface
    public void onFailure(final int httpStatus, final String statusMessage) {
        logger.debug("IntAdnJSCallback", "onFailure: {0} - {1}", httpStatus, statusMessage);
        this.handler.onFailure(view,statusMessage + " (" + httpStatus + ") error returned");
    }

    @JavascriptInterface
    public void log(final String context, final String message) {
        this.logger.debug(context, message);
    }

    @JavascriptInterface
    public void version(final String version) {
        this.logger.debug("version", version);
    }
}
