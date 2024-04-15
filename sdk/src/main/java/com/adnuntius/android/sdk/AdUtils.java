/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk;

import static com.adnuntius.android.sdk.AdnuntiusEnvironment.andemu;

import androidx.annotation.Nullable;

import java.util.Map;

final class AdUtils {
    private static final String JS_SHIM =
        "var adnSdkShim = new Object();\n" +
        "adnSdkShim.onDimsEvent = function(type, response) {\n" +
        "   if (response.hasOwnProperty('ads') && response.ads[0]) {\n" +
        "       var ad = response.ads[0]\n" +
        "       if (ad.hasOwnProperty('dims') && ad.hasOwnProperty('definedDims')) {\n" +
        "           intAndroidAdnuntius.onComplete(\n" +
        "               type,\n" +
        "               response.retAdCount || 0,\n" +
        "               ad.dims.w || 0,\n" +
        "               ad.dims.h || 0,\n" +
        "               ad.definedDims.w || 0,\n" +
        "               ad.definedDims.h || 0,\n" +
        "               ad.lineItemId || \"\",\n" +
        "               ad.creativeId || \"\"\n" +
        "           )\n" +
        "       }\n" +
        "   }\n" +
        "}\n" +
        "adnSdkShim.onVisible = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onVisible', JSON.stringify(response))\n" +
        "}\n" +
        "adnSdkShim.onRestyle = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onRestyle', JSON.stringify(response))\n" +
        "   adnSdkShim.onDimsEvent(\"restyle\", response)\n" +
        "}\n" +
        "adnSdkShim.onViewable = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onViewable', JSON.stringify(response))\n" +
        "}\n" +
        "adnSdkShim.onPageLoad = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onPageLoad', JSON.stringify(response))\n" +
        "   adnSdkShim.onDimsEvent(\"pageLoad\", response)\n" +
        "}\n" +
        "adnSdkShim.onNoMatchedAds = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onNoMatchedAds', JSON.stringify(response))\n" +
        "  intAndroidAdnuntius.onNoMatchedAds();\n" +
        "}\n" +
        "adnSdkShim.onImpressionResponse = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onImpressionResponse', JSON.stringify(response))\n" +
        "}\n" +
        "adnSdkShim.onError = function(response) {\n" +
        "   //intAndroidAdnuntius.log('onError', JSON.stringify(response))\n" +
        // this is a XMLHttpRequest object with a failed response from adn.js
        "   if (response.hasOwnProperty('args') && response.args[0]) {\n" +
        "       var object = response.args[0]\n" +
        "       if ('response' in object && 'status' in object) {\n" +
        "           intAndroidAdnuntius.onFailure(object['status'], object['response'])\n" +
        "       }\n" +
        "   }\n" +
        "}\n";

    private AdUtils() {
    }

    static String getAdScript(final AdnuntiusEnvironment env,
                              @Nullable final String adId,
                              final AdRequest request,
                              final String adUnitsJson,
                              final boolean isDebugEnabled) {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> e : request.parentParameters().entrySet()) {
            if (builder.length() > 0) {
                builder.append(",\n");
            }
            builder.append("                   ")
                    .append(e.getKey())
                    .append(": \"")
                    .append(e.getValue())
                    .append("\"");
        }

        final String externalId = adId != null ? "'" + adId + "'" : "null";
        final String impReg = adId != null ? "manual" : "auto";
        final String versionDebug = isDebugEnabled ? "intAndroidAdnuntius.version(adn.version)" : "// no version";

        return "<html>\n" +
                "<head>\n" +
                "   <style type=\"text/css\"> html, body { margin: 0px; padding: 0px; } </style>\n" +
                "   <script type=\"text/javascript\">\n" + getShimJs(isDebugEnabled) + "\n</script>\n" +
                "   <script type=\"text/javascript\" src=\"" + getAdnJsUrl(env) + "\" async></script>\n" +
                "</head>\n" +
                "   <body>\n" +
                "       <div id=\"adn-" + request.auId() + "\" style=\"display:none\"></div>\n" +
                "       <script type=\"text/javascript\">\n" +
                "           window.adn = window.adn || {}; adn.calls = adn.calls || [];\n" +
                "           adn.calls.push(function() {\n" +
                "               " + versionDebug + ",\n" +
                "               adn.request({\n" +
                "                   env: '" + env.name() + "',\n" +
                "                   sdk: 'android:" + BuildConfig.VERSION_NAME + "',\n" +
                "                   impReg: '" + impReg + "',\n" +
                "                   externalId: " + externalId + ",\n" +
                "                   onPageLoad: adnSdkShim.onPageLoad,\n" +
                "                   onImpressionResponse: adnSdkShim.onImpressionResponse,\n" +
                "                   onNoMatchedAds: adnSdkShim.onNoMatchedAds,\n" +
                "                   onVisible: adnSdkShim.onVisible,\n" +
                "                   onViewable: adnSdkShim.onViewable,\n" +
                "                   onRestyle: adnSdkShim.onRestyle,\n" +
                "                   onError: adnSdkShim.onError,\n" +
                (request.useCookies() ? "" : "                   useCookies: false,\n")+
                (builder.length() == 0 ? "" : builder + ",\n") +
                "                   adUnits: [" + adUnitsJson + "]\n" +
                "               });\n" +
                "           });\n" +
                "       </script>" +
                "   </body>\n" +
                "</html>";
    }

    private static String getShimJs(final boolean isDebugEnabled) {
        if (isDebugEnabled) {
            return JS_SHIM.replace("//intAndroidAdnuntius.log", "intAndroidAdnuntius.log");
        } else {
            return JS_SHIM;
        }
    }

    static String getAdnJsUrl(final AdnuntiusEnvironment env) {
        if (env == andemu) {
            return "http://10.0.2.2:8001/adn.src.js";
        } else {
            // currently all other envs use prod cdn
            return "https://cdn.adnuntius.com/adn.js";
        }
    }
}
