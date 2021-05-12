package com.adnuntius.android.sdk.http;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.BuildConfig;
import com.adnuntius.android.sdk.data.DataTarget;

public class HttpUtils {
    public static String getAuthUrl(AdnuntiusEnvironment env) {
        if (env == AdnuntiusEnvironment.production) {
            return "https://api.adnuntius.com/api/authenticate";
        } else {
            return "https://api." + env.name() + ".adnuntius.com/api/authenticate";
        }
    }

    public static String getDeliveryUrl(final AdnuntiusEnvironment env) {
        if (env == AdnuntiusEnvironment.production) {
            return "https://delivery.adnuntius.com";
        } else {
            return "https://adserver." + env.name() + ".adnuntius.com";
        }
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
        if (env == AdnuntiusEnvironment.production) {
            return "https://data.adnuntius.com";
        } else {
            return "https://" + env.name() + ".data.adnuntius.com";
        }
    }
}
