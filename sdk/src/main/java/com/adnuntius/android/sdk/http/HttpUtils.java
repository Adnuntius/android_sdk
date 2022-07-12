package com.adnuntius.android.sdk.http;

import static com.adnuntius.android.sdk.AdnuntiusEnvironment.andemu;
import static com.adnuntius.android.sdk.AdnuntiusEnvironment.production;

import androidx.annotation.Nullable;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.BuildConfig;
import com.adnuntius.android.sdk.LivePreview;
import com.adnuntius.android.sdk.data.DataTarget;

public class HttpUtils {
    public static String getAuthUrl(AdnuntiusEnvironment env) {
        if (env == production) {
            return "https://api.adnuntius.com/api/authenticate";
        } else {
            return "https://api." + env.name() + ".adnuntius.com/api/authenticate";
        }
    }

    public static String getDeliveryUrl(final AdnuntiusEnvironment env, @Nullable LivePreview livePreview) {
        return getDeliveryUrl(env, "", livePreview);
    }

    public static String getDeliveryUrl(final AdnuntiusEnvironment env, String target, @Nullable LivePreview livePreview) {
        final String baseUrl;
        // for now we are testing with ads on production, probably should change to using local dev env just like for cdn.js
        if (env == production) {
            baseUrl = "https://delivery.adnuntius.com" + target;
        } else if (env == andemu) {
            baseUrl = "http://10.0.2.2:8078" + target + "?script-override=andemu";
        } else {
            baseUrl = "https://adserver." + env.name() + ".adnuntius.com" + target;
        }

        if (livePreview != null) {
<<<<<<< Updated upstream
            final String separator = env == andemu ? "&" : "?";
            return baseUrl
                    + separator
                    + "adn-lp-li=" + livePreview.getLpl() + (livePreview.getLpc() == null ? "" : "&adn-lp-c="
                    + livePreview.getLpc());
=======
            baseUrl.append("?adn-lp-li=" + livePreview.getLpl() + (livePreview.getLpc() == null ? "" : "&adn-lp-c=" + livePreview.getLpc()) + "&adn-hide-warning=true");
>>>>>>> Stashed changes
        }
        return baseUrl;
    }

    /**
     * @param env
     * @param target
     * @param sync use the synchronous visitor api endpoint
     * @return
     */
    public static String getDataUrl(AdnuntiusEnvironment env, final DataTarget target, final boolean sync) {
        return getDataUrlPrefix(env) + (target.equals(DataTarget.visitor) && sync ? "/synchronous/" : "/") + target.name() + "?sdk=android:" + BuildConfig.VERSION_NAME;
    }

    private static String getDataUrlPrefix(AdnuntiusEnvironment env) {
        if (env == production) {
            return "https://data.adnuntius.com";
        } else {
            return "https://" + env.name() + ".data.adnuntius.com";
        }
    }
}
