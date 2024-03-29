/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data;

import com.adnuntius.android.sdk.http.ErrorResponse;

public interface DataResponseHandler {
    void onSuccess();
    void onFailure(final ErrorResponse response);
}
