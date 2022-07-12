package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusJavascriptCallback {
    private final LoadAdHandler handler;

    public AdnuntiusJavascriptCallback(final LoadAdHandler handler) {
        this.handler = handler;
    }

    @JavascriptInterface
    public void closeView() {
        handler.onLayoutCloseView();
    }
}
