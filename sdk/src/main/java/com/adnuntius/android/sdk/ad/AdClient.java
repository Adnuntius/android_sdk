package com.adnuntius.android.sdk.ad;

import android.content.Context;

import com.adnuntius.android.sdk.AdRequest;
import com.adnuntius.android.sdk.AdRequests;
import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.BuildConfig;
import com.adnuntius.android.sdk.http.ErrorResponse;
import com.adnuntius.android.sdk.http.HttpClient;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpUtils;
import com.adnuntius.android.sdk.http.volley.VolleyHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * https://docs.adnuntius.com/adnuntius-advertising/requesting-ads/http-api
 *
 * A simple implementation of an interface to do json ad requests to adnuntius ad server
 */
public class AdClient {
    private final Gson gson;
    private final AdnuntiusEnvironment env;
    private final HttpClient httpClient;

    /**
     * Production endpoint
     */
    public AdClient(final Context context) {
        this(AdnuntiusEnvironment.production, new VolleyHttpClient(context));
    }

    public AdClient(final AdnuntiusEnvironment env, final HttpClient httpClient) {
        gson = new GsonBuilder().create();
        this.httpClient = httpClient;
        this.env = env;
    }

    public void request(final AdRequest request, final AdResponseHandler handler) {
        // the height and width are not sent to the server, only used for normal load ad
        request.setHeight(null);
        request.setWidth(null);

        final AdRequests adRequests = new AdRequests(request);
        final String adUnitsJson = gson.toJson(adRequests);

        final String impressionUrl = HttpUtils.getDeliveryUrl(env, null)
                + "/i" + "?format=json&sdk=android:" + BuildConfig.VERSION_NAME;
        httpClient.postJsonRequest(
                impressionUrl,
                adUnitsJson,
                new HttpResponseHandler() {
                    @Override
                    public void onFailure(final ErrorResponse response) {
                        handler.onFailure(response);
                    }

                    @Override
                    public void onSuccess(final String jsonResponse) {
                        final JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);
                        final AdResponse adResponse = getAdFromDeliveryResponse(response);
                        if (adResponse != null) {
                            handler.onSuccess(adResponse);
                        } else {
                            handler.onFailure(new ErrorResponse(400, "No ad"));
                        }
                    }
                });
    }

    private static AdResponse getAdFromDeliveryResponse(JsonObject response) {
        final JsonArray jArr = response.getAsJsonArray("adUnits");
        if (jArr.size() > 0) {
            final JsonObject ad = jArr.get(0).getAsJsonObject();
            final int adCount = ad.getAsJsonPrimitive("matchedAdCount").getAsInt();
            if (adCount > 0) {
                return new AdResponse(ad.getAsJsonPrimitive("html").getAsString());
            }
        }
        return null;
    }
}
