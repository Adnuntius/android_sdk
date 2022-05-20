package com.adnuntius.android.sdk;

import static android.util.Log.DEBUG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.util.Log;

import com.adnuntius.android.sdk.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AdnuntiusAdWebView extends WebView {
    private static final String TAG = "AdnuntiusAdWebView";

    private final Gson gson;
    private AdnuntiusEnvironment env;
    private final CompletionHandlerWrapper wrapper = new CompletionHandlerWrapper();

    public AdnuntiusAdWebView(final Context context) {
        this(context,null);
    }

    public AdnuntiusAdWebView(final Context context, final AttributeSet attrs) {
        this(context, attrs, AdnuntiusEnvironment.production);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public AdnuntiusAdWebView(final Context context,
                              final AttributeSet attrs,
                              final AdnuntiusEnvironment env) {
        super(context, attrs);
        this.env = env;
        this.gson = new GsonBuilder().create();

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAllowFileAccess(false);
        AdnuntiusAdWebViewClient webClient = new AdnuntiusAdWebViewClient(context, env, wrapper);
        this.setWebViewClient(webClient);
        AdnuntiusAdWebViewChromeClient chromeClient = new AdnuntiusAdWebViewChromeClient(wrapper);
        this.setWebChromeClient(chromeClient);

        this.addJavascriptInterface(new InternalAdnuntiusJavascriptCallback(env, wrapper), "intAndroidAdnuntius");
        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(wrapper), "androidAdnuntius");

        // make the name compatible with the iOS sdk
        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(wrapper), "adnSdkHandler");

        // https://stackoverflow.com/a/23844693
        boolean isDebuggable = ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * Provide facility to override the environment configuration
     * @param env
     */
    public void setEnvironment(AdnuntiusEnvironment env) {
        this.env = env;
    }

    /**
     * @see #loadAd
     *
     * @param config
     * @param handler
     */
    @Deprecated
    public void loadFromConfig(final AdConfig config, final CompletionHandler handler) {
        loadAd(config, handler);
    }

    public void loadAd(final AdRequest request, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        final String adUnitsJson = gson.toJson(request).replace('"', '\'');
        final String adScript = AdUtils.getAdScript(env, request, adUnitsJson);
        if (Log.isLoggable(TAG, DEBUG)) {
            Log.d(TAG, "Ad Script: " + adScript);
        }
        final String baseUrl = HttpUtils.getDeliveryUrl(env, request.livePreview());
        Log.d(TAG, "Base URL: " + baseUrl);
        loadDataWithBaseURL(baseUrl, adScript,"text/html", "UTF-8", null);
    }

    /**
     * This is for dev purposes mostly, so you can properly debug the webview by initially loading
     * a blank page so that you can attach the chrome debug tools to the web view before calling loadAd
     *
     * @see https://github.com/Adnuntius/android_sdk/wiki/Debug-Web-View
     */
    public void loadBlankPage() {
        loadDataWithBaseURL(HttpUtils.getDeliveryUrl(env, null), "<html><title>Blank Page</title><body><h1>This page left intentionally blank</h1></body></html>","text/html", "UTF-8", null);
    }

    /**
     * This method of loading ads into the web view is deprecated, because it does not have all the
     * smarts of loadAd(AdRequest request) which utilises adn.js
     *
     * @param requestJson
     * @param handler
     */
    @Deprecated
    public void loadFromApi(final String requestJson, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        final AdRequests requests = gson.fromJson(requestJson, AdRequests.class);
        if (requests.getAdUnits().size() != 1) {
            throw new IllegalArgumentException("Invalid Ad Request");
        }
        loadAd(requests.getAdUnits().get(0), handler);
    }
}
