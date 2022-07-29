/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

public class LoadAdHandlerWrapper implements LoadAdHandler {
    private LoadAdHandler delegate;

    public void setDelegate(final LoadAdHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        this.delegate = handler;
    }

    @Override
    public void onFailure(final AdnuntiusAdWebView view, final String message) {
        delegate.onFailure(view, message);
    }

    @Override
    public void onNoAdResponse(final AdnuntiusAdWebView view) {
        delegate.onNoAdResponse(view);
    }

    @Override
    public void onAdResponse(final AdnuntiusAdWebView view, final AdResponseInfo info) {
        delegate.onAdResponse(view, info);
    }

    @Override
    public void onAdResize(final AdnuntiusAdWebView view, final AdResponseInfo info) {
        delegate.onAdResize(view, info);
    }

    @Override
    public void onLayoutCloseView(final AdnuntiusAdWebView view) {
        delegate.onLayoutCloseView(view);
    }
}
