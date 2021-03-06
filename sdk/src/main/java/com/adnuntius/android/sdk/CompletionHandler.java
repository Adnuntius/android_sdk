package com.adnuntius.android.sdk;

public interface CompletionHandler {
    void onComplete(int adCount);
    void onFailure(String message);

    default void onClose() {
        // do nothing
    }
}
