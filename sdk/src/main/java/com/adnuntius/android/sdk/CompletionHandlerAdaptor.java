package com.adnuntius.android.sdk;

/*
No op implementation so clients can implement only the methods they care about
 */
public class CompletionHandlerAdaptor implements CompletionHandler {
    @Override
    public void onComplete(int adCount) {
    }

    @Override
    public void onFailure(String message) {
    }
}
