package com.adnuntius.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.adnuntius.android.sdk.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AdnuntiusAdWebView extends WebView {
    private final Gson gson;
    private final AdnuntiusEnvironment env;
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
        final String adScript = AdUtils.getAdScript(request.getAuId(), adUnitsJson, request.useCookies());
         loadDataWithBaseURL(HttpUtils.getDeliveryUrl(env, request.livePreview()), adScript, "text/html", "UTF-8", null);
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
