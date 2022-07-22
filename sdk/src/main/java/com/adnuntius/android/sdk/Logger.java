package com.adnuntius.android.sdk;

import android.util.Log;

/**
 * A little wrapper around logging so can have a bit easier time turning logging
 * on and off, especially useful to debug the SDK from an aar file
 */
public class Logger {
    private static final String TAG = "AdnuntiusAdWebView";

    private String tag = TAG;
    private boolean debugEnabled;
    private boolean verboseEnabled;

    public Logger() {
    }

    public Logger tag(final String tag) {
        this.tag = tag;
        return this;
    }

    public Logger enableDebug(final boolean b) {
        this.debugEnabled = b;
        return this;
    }

    public Logger enableVerbose(final boolean b) {
        this.verboseEnabled = b;
        return this;
    }

    public void debug(String context, String msg) {
        if (debugEnabled) {
            // ya I know its not debug, but when debug is enabled, I want to be
            // sure I always get the messages even when using the sdk from a aar
            log(context, msg);
        }
    }

    public void debug(String context, String msg, Object arg) {
        if (debugEnabled) {
            log(context, msg.replace("{0}", arg.toString()));
        }
    }

    public void verbose(String context, String msg, String arg) {
        if (verboseEnabled) {
            log(context, msg.replace("{0}", arg));
        }
    }

    public void debug(String context, String msg, String arg) {
        if (debugEnabled) {
            log(context, msg.replace("{0}", arg));
        }
    }

    public void debug(String context, String msg, int arg, String arg2) {
        if (debugEnabled) {
            log(context, msg.replace("{0}", String.valueOf(arg)
                    .replace("{1}", String.valueOf(arg2))));
        }
    }

    private void log(String context, String msg) {
        Log.i(tag, context + " " + msg);
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
