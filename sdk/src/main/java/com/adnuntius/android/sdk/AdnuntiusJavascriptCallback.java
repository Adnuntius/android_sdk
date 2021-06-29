package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusJavascriptCallback {
    private final CompletionHandler handler;

    public AdnuntiusJavascriptCallback(final CompletionHandler handler) {
        this.handler = handler;
    }

    @JavascriptInterface
    public void closeView() {
        handler.onClose();
    }
}
