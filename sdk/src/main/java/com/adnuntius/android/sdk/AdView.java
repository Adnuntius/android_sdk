package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class AdView extends WebView {
    public AdView(Context context) {
        super(context);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDomStorageEnabled(true);
        this.setWebChromeClient(new WebChromeClient());

        // https://stackoverflow.com/a/23844693
        boolean isDebuggable = ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public void loadForConfig(AdConfig config) {
        String adScript = config.toScript();
        loadDataWithBaseURL("https://adnuntius.com/", adScript, "text/html", "UTF-8", null);
    }
}
