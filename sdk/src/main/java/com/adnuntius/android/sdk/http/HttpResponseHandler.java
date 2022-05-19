package com.adnuntius.android.sdk.http;

public interface HttpResponseHandler {
    void onFailure(final ErrorResponse response);
    void onSuccess(final String response);
}
