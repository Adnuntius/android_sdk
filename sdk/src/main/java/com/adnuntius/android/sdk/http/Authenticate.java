/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http;

import com.google.gson.annotations.SerializedName;

public class Authenticate {
    @SerializedName("grant_type")
    private final String grantType;
    private final String scope;
    private final String username;
    private final String password;

    public Authenticate(final String username,
                        final String password) {
        this.grantType = "password";
        this.scope = "ng_api";
        this.username = username;
        this.password = password;
    }
}
