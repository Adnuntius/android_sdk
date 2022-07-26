/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusJavascriptCallback {
    private final AdnuntiusAdWebView view;
    private final LoadAdHandler handler;
    private final Logger logger;

    public AdnuntiusJavascriptCallback(final AdnuntiusAdWebView view, final LoadAdHandler handler, final Logger logger) {
        this.view = view;
        this.handler = handler;
        this.logger = logger;
    }

    @JavascriptInterface
    public void closeView() {
        logger.debug("AdnJSCallback", "closeView");
        handler.onLayoutCloseView(view);
    }
}
