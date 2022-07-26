/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adnuntius.android.sdk.http.HttpUtils;

public class AdnuntiusAdWebViewClient extends WebViewClient {
    private final Context context;
    private final LoadAdHandler handler;
    private final AdnuntiusEnvironment env;
    private final Logger logger;

    public AdnuntiusAdWebViewClient(final Context context,
                                    final AdnuntiusEnvironment env,
                                    final LoadAdHandler handler,
                                    final Logger logger) {
        this.context = context;
        this.env = env;
        this.handler = handler;
        this.logger = logger;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        final Uri url = request.getUrl() == null ? null : request.getUrl();
        if (url != null) {
            logger.debug("shouldOverrideUrlLoading", "URL: {0}", url);
            if (HttpUtils.getDeliveryUrl(env, null).equals(url.getHost())) {
                return false;
            }
            final Intent intent = new Intent(Intent.ACTION_VIEW, url);
            context.startActivity(intent);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        final Uri url = request.getUrl() == null ? null : request.getUrl();
        if (url != null) {
            logger.debug("shouldInterceptRequest", "URL: {0}", url);

            final String path = url.getPath() == null ? null : url.getPath();
            if (path != null && path.endsWith("/favicon.ico")) {
                try {
                    return new WebResourceResponse("image/png", null, null);
                } catch (Exception e) {
                }
            }
        }
        return super.shouldInterceptRequest(view, request);
    }
}
