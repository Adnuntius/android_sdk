package com.adnuntius.android.sdk;

import static android.util.Log.DEBUG;

import android.util.Log;

public class Logger {
    private static final String TAG = "AdnuntiusAdWebView";

    public void debug(String msg) {
        if (Log.isLoggable(TAG, DEBUG)) {
            Log.d(TAG, msg);
        }
    }

    public boolean isDebugEnabled() {
        return Log.isLoggable(TAG, DEBUG);
    }
}
