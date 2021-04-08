package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdnuntiusAdWebView extends WebView {
    private static final String BASE_URL = "https://delivery.adnuntius.com/";

    private final CompletionHandlerWrapper wrapper = new CompletionHandlerWrapper();

    public AdnuntiusAdWebView(Context context) {
        super(context);
    }

    public AdnuntiusAdWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAllowFileAccess(false);
        AdnuntiusAdWebViewClient webClient = new AdnuntiusAdWebViewClient(context, wrapper);
        this.setWebViewClient(webClient);
        AdnuntiusAdWebViewChromeClient chromeClient = new AdnuntiusAdWebViewChromeClient(wrapper);
        this.setWebChromeClient(chromeClient);

        this.addJavascriptInterface(new AdnuntiusJavascriptCallback(wrapper), "adnuntius");

        // https://stackoverflow.com/a/23844693
        boolean isDebuggable = ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public void loadFromConfig(final AdConfig config, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        String adScript = config.toScript();
        String shimmedAdScript = JsShimUtils.injectShim(adScript);
        loadDataWithBaseURL(BASE_URL, shimmedAdScript, "text/html", "UTF-8", null);
    }

    public void loadFromApi(final String jsonConfig, final CompletionHandler handler) {
        this.wrapper.setDelegate(handler);

        try {
            JSONObject config = new JSONObject(jsonConfig);

            final JsonObjectRequest jsonobj = new JsonObjectRequest(
                    Request.Method.POST, "https://delivery.adnuntius.com/i?format=json&sdk=android:" + BuildConfig.VERSION_NAME, config,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jArr = response.getJSONArray("adUnits");
                                if (jArr.length() > 0) {
                                    JSONObject ad = jArr.getJSONObject(0);
                                    int adCount = ad.getInt("matchedAdCount");
                                    if (adCount > 0) {
                                        String shimmedAdScript = JsShimUtils.injectShim(ad.getString("html"));
                                        loadDataWithBaseURL(BASE_URL, shimmedAdScript, "text/html", "UTF-8", null);
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                // going assume there was nothing returned and treat as no ad
                            }

                            handler.onComplete(0);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handler.onFailure(error.getMessage());
                        }
                    }
            );

            RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(jsonobj);
        } catch (JSONException e) {
            // this is a parsing exception pure and simple so immediately throw it
            throw new IllegalArgumentException(e);
        }
    }
}
