package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusJavascriptCallback {
    private final CompletionHandler handler;
    private final AdnuntiusEnvironment env;

    public AdnuntiusJavascriptCallback(final AdnuntiusEnvironment env, final CompletionHandler handler) {
        this.handler = handler;
        this.env = env;
    }

    @JavascriptInterface
    public void onComplete(String url, int adCount) {
        // only register an oncomplete for an impression, everything else is callbacks
        if (url.contains(HttpUtils.getDeliveryUrl(env) + "/i")) {
            this.handler.onComplete(adCount);
        }
    }

    @JavascriptInterface
    public void onFailure(String url, int httpStatus) {
        this.handler.onFailure(httpStatus + " error returned for " + url);
    }
}
