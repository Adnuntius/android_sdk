/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

@Deprecated
public class LoadAdCompletionHandlerAdaptor implements LoadAdHandler {
    private final CompletionHandler delegate;

    public LoadAdCompletionHandlerAdaptor(final CompletionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onFailure(final AdnuntiusAdWebView view, String message) {
        delegate.onFailure(message);
    }

    @Override
    public void onNoAdResponse(final AdnuntiusAdWebView view) {
        delegate.onComplete(0);
    }

    @Override
    public void onAdResponse(final AdnuntiusAdWebView view, final AdResponseInfo info) {
        delegate.onComplete(1);
    }

    @Override
    public void onLayoutCloseView(final AdnuntiusAdWebView view) {
        delegate.onClose();
    }
}
