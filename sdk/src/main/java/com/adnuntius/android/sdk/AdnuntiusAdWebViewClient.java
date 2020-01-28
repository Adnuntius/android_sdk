package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdnuntiusAdWebViewClient extends WebViewClient {
    private static final String TAG = "AdnuntiusSDKWebView";

    private final Context context;
    private final CompletionHandler handler;

    public AdnuntiusAdWebViewClient(final Context context, final CompletionHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
        if ("delivery.adnuntius.com".equals(url.getUrl().getHost())) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, url.getUrl());
        context.startActivity(intent);
        return true;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        // just a bit of overkill to avoid npe
        if(request.getUrl() != null
                && request.getUrl().getPath() != null
                && request.getUrl().getPath().endsWith("/favicon.ico")) {
            try {
                return new WebResourceResponse("image/png", null, null);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public void onLoadResource(WebView view, String url) {
        Log.d(TAG, url);
    }
}
