/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.ad;

import com.adnuntius.android.sdk.http.ErrorResponse;

public interface AdResponseHandler {
    void onSuccess(final AdResponse response);
    void onFailure(final ErrorResponse response);
}
