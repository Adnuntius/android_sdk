package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.adnuntius.android.sdk.ad.AdClient;
import com.adnuntius.android.sdk.ad.AdResponseHandler;
import com.adnuntius.android.sdk.ad.AdUtils;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpUtils;
import com.adnuntius.android.sdk.http.volley.VolleyHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AdnuntiusAdWebView extends WebView {
    private final Gson gson;
    private final CompletionHandlerWrapper wrapper = new CompletionHandlerWrapper();
    private final AdClient adClient;

    public AdnuntiusAdWebView(final Context context) {
        this(context,null);
    }

    public AdnuntiusAdWebView(final Context context, final AttributeSet attrs) {
        this(context, attrs, new AdClient(AdnuntiusEnvironment.production, new VolleyHttpClient(context)));
    }

    public AdnuntiusAdWebView(final Context context,
                              final AttributeSet attrs,
                              final AdClient adClient) {
        super(context, attrs);

        this.adClient = adClient;
        this.gson = new GsonBuilder().create();

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAllowFileAccess(false);
        AdnuntiusAdWebViewClient webClient = new AdnuntiusAdWebViewClient(context, adClient.getEnv(), wrapper);
        this.setWebViewClient(webClient);
        AdnuntiusAdWebViewChromeClient chromeClient = new AdnuntiusAdWebViewChromeClient(wrapper);
        this.setWebChromeClient(chromeClient);

        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(adClient.getEnv(), wrapper), "adnuntius");

        // https://stackoverflow.com/a/23844693
        boolean isDebuggable = ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * @see loadAd
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

        final String jsJsonConfigString = gson.toJson(request).replace('"', '\'');
        final String adScript = AdUtils.getAdScript(request.getAuId(), jsJsonConfigString);
        final String shimmedAdScript = AdUtils.injectShim(adScript);
         loadDataWithBaseURL(HttpUtils.getDeliveryUrl(adClient.getEnv()), shimmedAdScript, "text/html", "UTF-8", null);
    }

    /**
     * This method of loading ads into the web view is deprecated, because it does not have all the
     * smarts of loadAd(AdRequest request) which utilises adn.js
     *
     * @see AdClient
     *
     * @param requestJson
     * @param handler
     */
    @Deprecated
    public void loadFromApi(final String requestJson, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        adClient.request(requestJson, new AdResponseHandler() {
            @Override
            public void onSuccess(JsonObject response) {
                try {
                    final AdUtils.AdResponse adScript = AdUtils.getAdFromDeliveryResponse(response);
                    if (adScript != null) {
                        final String shimmedAdScript = AdUtils.injectShim(adScript.getHtml());
                        loadDataWithBaseURL(HttpUtils.getDeliveryUrl(adClient.getEnv()), shimmedAdScript, "text/html", "UTF-8", null);

                        // its a bit odd, but because we already did the /i above we should trigger the oncomplete now
                        handler.onComplete(adScript.getAdCount());
                        return;
                    }
                } catch (final Exception e) {
                    // going assume there was nothing returned and treat as no ad
                }

                // no ad returned
                handler.onComplete(0);
            }

            @Override
            public void onFailure(ErrorResponse response) {
                handler.onFailure(response.getMessage());
            }
        });
    }
}
