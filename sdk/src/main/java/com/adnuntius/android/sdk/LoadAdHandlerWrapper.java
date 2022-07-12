package com.adnuntius.android.sdk;

public class LoadAdHandlerWrapper implements LoadAdHandler {
    private LoadAdHandler delegate;

    public void setDelegate(LoadAdHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        this.delegate = handler;
    }

    @Override
    public void onFailure(String message) {
        delegate.onFailure(message);
    }

    @Override
    public void onNoAdResponse() {
        delegate.onNoAdResponse();
    }

    @Override
    public void onAdResponse(AdResponseInfo info) {
        delegate.onAdResponse(info);
    }

    @Override
    public void onAdResize(AdResponseInfo info) {
        delegate.onAdResize(info);
    }

    @Override
    public void onLayoutCloseView() {
        delegate.onLayoutCloseView();
    }
}
