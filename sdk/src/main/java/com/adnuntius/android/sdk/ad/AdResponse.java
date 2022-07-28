/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.ad;

public class AdResponse {
    private final String html;

    public AdResponse(final String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }
}