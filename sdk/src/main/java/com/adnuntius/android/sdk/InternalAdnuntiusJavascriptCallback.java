package com.adnuntius.android.sdk;

import android.webkit.JavascriptInterface;

import com.adnuntius.android.sdk.http.HttpUtils;

public class InternalAdnuntiusJavascriptCallback {
    private final CompletionHandler handler;
    private final AdnuntiusEnvironment env;

    public InternalAdnuntiusJavascriptCallback(final AdnuntiusEnvironment env, final CompletionHandler handler) {
        this.handler = handler;
        this.env = env;
    }

    @JavascriptInterface
    public void onComplete(int adCount) {
        this.handler.onComplete(adCount);
    }

    @JavascriptInterface
    public void onFailure(int httpStatus, String statusMessage) {
        this.handler.onFailure(statusMessage + " (" + httpStatus + ") error returned");
    }
}
