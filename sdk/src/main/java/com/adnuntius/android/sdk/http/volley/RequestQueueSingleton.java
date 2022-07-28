/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.http.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// https://developer.android.com/training/volley/requestqueue
public class RequestQueueSingleton implements HttpRequestQueue {
    private static RequestQueueSingleton instance;
    private RequestQueue requestQueue;

    private final Context ctx;

    public RequestQueueSingleton(Context ctx) {
        this.ctx = ctx;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    @Override
    public <T> void add(Request<T> req) {
        getRequestQueue().add(req);
    }
}
