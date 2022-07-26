/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.adnuntius.android.sdk.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

public class AdnuntiusAdWebView extends WebView {
    // the updateView method gets called a lot reduce object gargage by reusing
    private final Rect childRect = new Rect();
    private final Rect intersection = new Rect();
    private final Rect scrollBounds = new Rect();

    private final Gson gson;
    private String adId;
    private AdnuntiusEnvironment env;
    private final LoadAdHandlerWrapper wrapper = new LoadAdHandlerWrapper();
    public final Logger logger = new Logger();
    private boolean hasVisibleCalled = false;
    private boolean hasViewableCalled = false;

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
        AdnuntiusAdWebViewClient webClient = new AdnuntiusAdWebViewClient(context, env, wrapper, logger);
        this.setWebViewClient(webClient);
        AdnuntiusAdWebViewChromeClient chromeClient = new AdnuntiusAdWebViewChromeClient(this, wrapper, logger);
        this.setWebChromeClient(chromeClient);

        this.addJavascriptInterface(new InternalAdnuntiusJavascriptCallback(this, env, wrapper, logger), "intAndroidAdnuntius");
        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(this, wrapper, logger), "androidAdnuntius");

        // make the name compatible with the iOS sdk
        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(this, wrapper, logger), "adnSdkHandler");

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
     * @see #loadAd(AdRequest, LoadAdHandler)
     */
    @Deprecated
    public void loadAd(final AdRequest request, final CompletionHandler handler) {
        loadAd(request, false, new LoadAdCompletionHandlerAdaptor(handler));
    }

    /**
     An ad loaded via this method will not register viewable or visible events, that
     will only happen if the updateView methods are called afterwards
     */
    public void loadAd(final AdRequest request, final LoadAdHandler handler) {
        loadAd(request, true, handler);
    }

    /**
     * @param request  The Ad Request configuration
     * @param delayVisibleEvents - if true will delay visible and viewable events until updateView(...) is called.
     *                           If false will generate visible and viewable events as soon as the ad is rendered.
     * @param handler
     *
     * Warning: Before release 1.7.0 of the Android SDK, calls to loadAd would generate viewable and visible events
     * as soon as the ad was rendered but before it was visible in the device viewport.
     *
     * From 1.7.0 onwards, this version of loadAd visible and viewable events will not be immediately generated.
     * If you want the old behaviour use `loadAd(request. false, handler)`
     */
    public void loadAd(final AdRequest request, final boolean delayVisibleEvents, final LoadAdHandler handler) {
        this.wrapper.setDelegate(handler);

        this.hasVisibleCalled = false;
        this.hasViewableCalled = false;

        final String adUnitsJson = gson.toJson(request).replace('"', '\'');
        this.adId = delayVisibleEvents ? UUID.randomUUID().toString() : null;
        final String adScript = AdUtils.getAdScript(env, adId, request, adUnitsJson, logger.isDebugEnabled());
        logger.verbose("loadAd", "Ad Script: [{0}]", adScript);
        final String baseUrl = HttpUtils.getDeliveryUrl(env, request.livePreview());
        logger.debug("loadAd", "Base URL: {0}", baseUrl);
        loadDataWithBaseURL(baseUrl, adScript,"text/html", "UTF-8", null);
    }

    /**
     This method is used by calling code to notify the AdnuntiusWebView of potential changes in its visibility
     in the device view port.

     Currently is supported for ScrollView only.
     */
    public void updateView(ScrollView scrollView) {
        if (this.adId == null) {
            this.logger.debug("updateView", "loadAd not called or delayViewEvents=false");
            return;
        }

        // no point if both events are fired, just a waste of time
        if (this.hasViewableCalled && this.hasVisibleCalled) {
            return;
        }

        getDrawingRect(childRect);
        if (scrollView.getGlobalVisibleRect(scrollBounds)) {
            if (getGlobalVisibleRect(intersection)) {
                final int percentage = RectUtils.percentageContains(scrollBounds, childRect, intersection);
                this.logger.debug("updateView", "Percentage {0}", percentage);
                if (percentage > 0 && !this.hasVisibleCalled) {
                    this.hasVisibleCalled = true;
                    registerEvent("Visible");
                }

                if (percentage >= 50 && !this.hasViewableCalled) {
                    this.hasViewableCalled = true;
                    registerEvent("Viewable");
                }
            }
        }
    }

    private void registerEvent(final String event) {
        this.logger.debug("updateView", "Register Event {0}", event);

        final String eventJsScript =
                "window.adn = window.adn || {}; adn.calls = adn.calls || [];\n" +
                "adn.calls.push(function() {\n" +
                "   adn.reg" + event + "({externalId: '" + this.adId + "'})\n" +
                "})";
        this.post(() -> evaluateJavascript(eventJsScript, value -> {}));
    }

    /**
     * This is for dev purposes mostly, so you can properly debug the webview by initially loading
     * a blank page so that you can attach the chrome debug tools to the web view before calling loadAd
     *
     * refer to https://github.com/Adnuntius/android_sdk/wiki/Debug-Web-View
     */
    public void loadBlankPage() {
        loadDataWithBaseURL(HttpUtils.getDeliveryUrl(env, null), "<html><body></body></html>","text/html", "UTF-8", null);
    }
}
