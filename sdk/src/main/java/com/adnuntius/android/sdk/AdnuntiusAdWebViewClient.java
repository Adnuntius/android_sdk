package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AdnuntiusAdWebViewClient extends WebViewClient {
    private final Context context;
    private final CompletionHandler handler;

    public AdnuntiusAdWebViewClient(final Context context, final CompletionHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
        if ("delivery.adnuntius.com".equals(url.getUrl().getHost())) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, url.getUrl());
        context.startActivity(intent);
        return true;
    }
}
