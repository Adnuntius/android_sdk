package com.adnuntius.android.sdk;

@Deprecated
public class LoadAdCompletionHandlerAdaptor implements LoadAdHandler {
    private final CompletionHandler delegate;

    public LoadAdCompletionHandlerAdaptor(final CompletionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onFailure(String message) {
        delegate.onFailure(message);
    }

    @Override
    public void onNoAdResponse() {
        delegate.onComplete(0);
    }

    @Override
    public void onAdResponse(final AdResponseInfo info) {
        delegate.onComplete(1);
    }

    @Override
    public void onLayoutCloseView() {
        delegate.onClose();
    }
}
