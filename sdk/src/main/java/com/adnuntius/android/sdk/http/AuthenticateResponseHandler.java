/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http;

public interface AuthenticateResponseHandler {
    void onFailure(final ErrorResponse response);
    void onSuccess(final BearerToken token);
}
