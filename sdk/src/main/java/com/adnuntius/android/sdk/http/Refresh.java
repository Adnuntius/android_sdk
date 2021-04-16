package com.adnuntius.android.sdk.http;

import com.google.gson.annotations.SerializedName;

public class Refresh {
    @SerializedName("grant_type")
    private final String grantType;
    private final String scope;
    @SerializedName("refresh_token")
    private final String refreshToken;

    public Refresh(final String refreshToken) {
        this.grantType = "refresh_token";
        this.scope = "ng_api";
        this.refreshToken = refreshToken;
    }
}
