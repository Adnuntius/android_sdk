package com.adnuntius.android.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void loadAdFromScript(String adScript) {
        loadDataWithBaseURL("https://adnuntius.com/", adScript, "text/html", "UTF-8", null);
    }

    public void loadAdFromJson(String jsonConfig) {
        try {
            JSONObject config = new JSONObject(jsonConfig);

            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, "https://delivery.adnuntius.com/i?format=json", config,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jArr = response.getJSONArray("adUnits");
                                JSONObject ad = jArr.getJSONObject(0);
                                loadAdFromScript(ad.getString("html"));
                            } catch (Exception e) {
                                Log.d("debug app", "Failed to parse json response", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("debug app", error.getMessage());
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
