package com.adnuntius.android.sdk;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class AdnuntiusAdWebViewChromeClient extends WebChromeClient {
    private final LoadAdHandler handler;

    public AdnuntiusAdWebViewChromeClient(final LoadAdHandler handler) {
        this.handler = handler;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (consoleMessage.message().contains("Unable to find HTML element")) {
            handler.onFailure(consoleMessage.message());
        }

        // we only want to access the message, but let the web view process as normal
        return false;
    }
}
