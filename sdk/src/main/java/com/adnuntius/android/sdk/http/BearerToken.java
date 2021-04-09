package com.adnuntius.android.sdk.http;

import com.google.gson.annotations.SerializedName;

public class BearerToken {
    private final transient long created;

    private String username;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Long expiresInSeconds;

    @SerializedName("refresh_token")
    private String refreshToken;

    public BearerToken() {
        this.created = System.currentTimeMillis();
    }

    /**
     * For testing
     */
    BearerToken(final long currentTimeMillis, final String username, final String tokenType, final String accessToken, final Long expiresInSeconds, final String refreshToken) {
        this.created = currentTimeMillis;
        this.username = username;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresInSeconds = expiresInSeconds;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    /**
     * For testing
     */
    boolean isExpired(final long currentTimeMillis) {
        final long expired = created + (expiresInSeconds * 1000);
        return currentTimeMillis >= expired;
    }
}
