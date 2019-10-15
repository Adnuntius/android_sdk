package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

public class AdnuntiusJavascriptCallback {
    private final CompletionHandler handler;

    public AdnuntiusJavascriptCallback(CompletionHandler handler) {
        this.handler = handler;
    }

    @JavascriptInterface
    public void onComplete(String url, int adCount) {
        // only register an oncomplete for an impression, everything else is callbacks
        if (url.contains("delivery.adnuntius.com/i")) {
            this.handler.onComplete(adCount);
        }
    }

    @JavascriptInterface
    public void onFailure(String url, int httpStatus) {
        this.handler.onFailure(httpStatus + " error returned for " + url);
    }
}
