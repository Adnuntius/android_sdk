package com.adnuntius.android.sdk;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class AdnuntiusAdWebViewChromeClient extends WebChromeClient {
    private final LoadAdHandler handler;
    private final Logger logger;

    public AdnuntiusAdWebViewChromeClient(final LoadAdHandler handler, final Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        logger.debug("onConsoleMessage", consoleMessage.message());

        if (consoleMessage.message().contains("Unable to find HTML element")) {
            handler.onFailure(consoleMessage.message());
        }

        // we only want to access the message, but let the web view process as normal
        return false;
    }
}
