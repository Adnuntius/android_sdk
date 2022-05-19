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
    private static final String TAG = "AdnuntiusWebViewClient";

    private final Context context;
    private final CompletionHandler handler;
    private final AdnuntiusEnvironment env;

    public AdnuntiusAdWebViewClient(final Context context, final AdnuntiusEnvironment env, final CompletionHandler handler) {
        this.context = context;
        this.env = env;
        this.handler = handler;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
        if (HttpUtils.getDeliveryUrl(env, null).equals(url.getUrl().getHost())) {
            return false;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW, url.getUrl());
        context.startActivity(intent);
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        final Uri url = request.getUrl() == null ? null : request.getUrl();
        if (url != null) {
            Log.i(TAG, url.toString());

            final String path = url.getPath() == null ? null : url.getPath();
            // just a bit of overkill to avoid npe
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
