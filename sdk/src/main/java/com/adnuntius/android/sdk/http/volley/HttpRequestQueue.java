/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import com.android.volley.Request;

/**
 * Allows testing of volley without the request queue singleton
 */
public interface HttpRequestQueue {
    <T> void add(final Request<T> req);
}
