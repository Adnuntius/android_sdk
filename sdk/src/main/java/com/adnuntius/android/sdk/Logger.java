/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.util.Log;

/**
 * A little wrapper around logging so can have a bit easier time turning logging
 * on and off, especially useful to debug the SDK from an aar file
 */
public class Logger {
    private static final String TAG = "AdnuntiusAdWebView";

    public String id = TAG;
    public boolean debug;
    public boolean verbose;

    public Logger() {
    }

    @Deprecated
    public Logger enableDebug(final boolean b) {
        this.debug = b;
        return this;
    }

    public void debug(String context, String msg) {
        if (debug) {
            // ya I know its not debug, but when debug is enabled, I want to be
            // sure I always get the messages even when using the sdk from a aar
            log(context, msg);
        }
    }

    public void debug(String context, String msg, Object arg) {
        if (debug) {
            log(context, msg.replace("{0}", arg.toString()));
        }
    }

    public void verbose(String context, String msg, String arg) {
        if (verbose) {
            log(context, msg.replace("{0}", arg));
        }
    }

    public void debug(String context, String msg, String arg) {
        if (debug) {
            log(context, msg.replace("{0}", arg));
        }
    }

    public void debug(String context, String msg, int arg, String arg2) {
        if (debug) {
            log(context, msg.replace("{0}", String.valueOf(arg)
                    .replace("{1}", String.valueOf(arg2))));
        }
    }

    private void log(String context, String msg) {
        Log.i(id, context + " " + msg);
    }

    public boolean isDebugEnabled() {
        return debug;
    }
}
