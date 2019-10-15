package com.adnuntius.android.sdk;

public interface CompletionHandler {
    public void onComplete(int adCount);
    public void onFailure(String message);
}
