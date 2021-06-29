package com.adnuntius.android.sdk;

public class CompletionHandlerWrapper implements CompletionHandler {
    private CompletionHandler delegate;

    public void setDelegate(CompletionHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        this.delegate = handler;
    }

    @Override
    public void onComplete(int adCount) {
        if (delegate != null) {
            delegate.onComplete(adCount);
        }
    }

    @Override
    public void onFailure(String error) {
        if (delegate != null) {
            delegate.onFailure(error);
        }
    }

    @Override
    public void onClose() {
        delegate.onClose();
    }
}
