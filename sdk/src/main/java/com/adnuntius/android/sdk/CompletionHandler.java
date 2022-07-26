/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

@Deprecated
public interface CompletionHandler {
    void onComplete(int adCount);
    void onFailure(String message);

    default void onClose() {
        // do nothing
    }
}
