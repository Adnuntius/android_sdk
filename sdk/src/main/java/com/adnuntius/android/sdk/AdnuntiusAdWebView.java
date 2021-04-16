package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpClient;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpUtils;
import com.adnuntius.android.sdk.http.volley.VolleyHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AdnuntiusAdWebView extends WebView {


    private final Gson gson;
    private final CompletionHandlerWrapper wrapper = new CompletionHandlerWrapper();
    private final HttpClient httpClient;
    private final AdnuntiusEnvironment env;
    private final String deliveryUrl;

    public AdnuntiusAdWebView(final Context context) {
        this(context,null);
    }

    public AdnuntiusAdWebView(final Context context, final AttributeSet attrs) {
        this(context, attrs, new VolleyHttpClient(context), AdnuntiusEnvironment.production);
    }

    public AdnuntiusAdWebView(final Context context,
                              final AttributeSet attrs,
                              final HttpClient httpClient,
                              final AdnuntiusEnvironment env) {
        super(context, attrs);

        this.env = env;
        this.deliveryUrl = HttpUtils.getDeliveryUrl(env);

        this.httpClient = httpClient;
        this.gson = new GsonBuilder().create();

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAllowFileAccess(false);
        AdnuntiusAdWebViewClient webClient = new AdnuntiusAdWebViewClient(context, env, wrapper);
        this.setWebViewClient(webClient);
        AdnuntiusAdWebViewChromeClient chromeClient = new AdnuntiusAdWebViewChromeClient(wrapper);
        this.setWebChromeClient(chromeClient);

        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(env, wrapper), "adnuntius");

        // https://stackoverflow.com/a/23844693
        boolean isDebuggable = ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public void loadFromConfig(final AdConfig config, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        final String jsJsonConfigString = gson.toJson(config).replace('"', '\'');
        final String adScript = AdUtils.getAdScript(config.getAuId(), jsJsonConfigString);
        final String shimmedAdScript = AdUtils.injectShim(adScript);
         loadDataWithBaseURL(deliveryUrl, shimmedAdScript, "text/html", "UTF-8", null);
    }

    /**
     * This method of loading ads into the web view is deprecated, because it does not have all the
     * smarts of loadFromConfig which relies on adn.js
     *
     * @param jsonConfig
     * @param handler
     */
    @Deprecated
    public void loadFromApi(final String jsonConfig, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        httpClient.postJsonRequest(deliveryUrl + "/i?format=json&sdk=android:" + BuildConfig.VERSION_NAME,
            jsonConfig,
            new HttpResponseHandler() {
                @Override
                public void onFailure(final ErrorResponse message) {
                    handler.onFailure(message.getMessage());
                }

                @Override
                public void onSuccess(final String response) {
                    try {
                        final JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                        final AdUtils.AdResponse adScript = AdUtils.getAdFromDeliveryResponse(responseJson);
                        if (adScript != null) {
                            final String shimmedAdScript = AdUtils.injectShim(adScript.getHtml());
                            loadDataWithBaseURL(deliveryUrl, shimmedAdScript, "text/html", "UTF-8", null);

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
            }
            );
    }


}
