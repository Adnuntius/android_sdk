package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusJavascriptCallback {
    private final LoadAdHandler handler;
    private final Logger logger;

    public AdnuntiusJavascriptCallback(final LoadAdHandler handler, final Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    @JavascriptInterface
    public void closeView() {
        logger.debug("AdnJSCallback", "closeView");
        handler.onLayoutCloseView();
    }
}
