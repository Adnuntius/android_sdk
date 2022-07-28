/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import androidx.annotation.Nullable;

public class LivePreview {
    private final String lpl;
    private final String lpc;

    public LivePreview(final String lpl, @Nullable final String lpc) {
        this.lpl = lpl;
        this.lpc = lpc;
    }

    public String getLpl() {
        return lpl;
    }

    public String getLpc() {
        return lpc;
    }
}
