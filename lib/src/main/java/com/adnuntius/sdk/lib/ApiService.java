package com.adnuntius.sdk.lib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;

import org.json.JSONObject;

public class ApiService extends Activity {
    public static void getAds(JSONObject config, Context ctx, final ICallback mResultCallback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, "https://delivery.adnuntius.com/i?format=json", config,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mResultCallback.callback(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Debug err", error.toString());
                    }
                }
        ){
        };
        requestQueue.add(jsonobj);

    }
}