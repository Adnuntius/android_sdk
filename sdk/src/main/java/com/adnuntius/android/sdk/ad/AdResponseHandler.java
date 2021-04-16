package com.adnuntius.android.sdk.ad;

import com.adnuntius.android.sdk.http.ErrorResponse;
import com.google.gson.JsonObject;

public interface AdResponseHandler {
    void onSuccess(final JsonObject response);
    void onFailure(final ErrorResponse response);
}
