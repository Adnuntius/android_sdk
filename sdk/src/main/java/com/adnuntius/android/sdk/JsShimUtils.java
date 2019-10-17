package com.adnuntius.android.sdk;

public final class JsShimUtils {
    private static final String JS_SHIM =
        "var adnSdkShim = new Object();\n" +
        "adnSdkShim.reallyOpen = XMLHttpRequest.prototype.open;\n" +
        "adnSdkShim.reallySend = XMLHttpRequest.prototype.send;\n" +
        "\n" +
        "adnSdkShim.ajaxEvent = function(url, status, response) {\n" +
        "   if (status == 200) {\n" +
        "       var adCount = this.getAdsCount(response)\n" +
    "           adnuntius.onComplete(url, adCount);\n" +
        "   } else {\n" +
        "       adnuntius.onFailure(url, status);\n" +
        "   }\n" +
        "}\n" +
        "\n" +
        "XMLHttpRequest.prototype.open = function(method, url, async, user, password) {\n" +
        "   url = url + \"&sdk=android:" + BuildConfig.VERSION_NAME + "\";\n" +
        "   adnSdkShim.reallyOpen.apply(this, arguments);\n" +
        "   adnSdkShim.url = url;\n" +
        "}\n" +
        "\n" +
        "XMLHttpRequest.prototype.send = function(data) {\n" +
        "   var callback = this.onreadystatechange;\n" +
        "   this.onreadystatechange = function() {\n" +
        "       if (this.readyState == 4) {\n" +
        "           try {\n" +
        "               adnSdkShim.ajaxEvent(adnSdkShim.url, this.status, this.response);\n" +
        "           } catch(e) {}\n" +
        "       }\n" +
        "       callback.apply(this, arguments);\n" +
        "   }\n" +
        "   adnSdkShim.reallySend.apply(this, arguments);\n" +
        "}\n" +
        "\n" +
        "adnSdkShim.getAdsCount = function(response) {\n" +
        "   var totalCount = 0\n" +
        "   try {\n" +
        "       var obj = JSON.parse(response)\n" +
        "       if (obj.adUnits != undefined) {\n" +
        "           obj.adUnits.forEach(function (item, index) {\n" +
        "               var count = item.matchedAdCount\n" +
        "               totalCount += count\n" +
        "           });\n" +
        "       }\n" +
        "   } catch(e) {\n" +
        "   }\n" +
        "   return totalCount\n" +
        "}";

    private JsShimUtils() {
    }

    public static String injectShim(final String script) {
        String tmpScript = script
                .replaceAll("<head\\s*/>", "<head></head>");

        int indexOf = tmpScript.indexOf("<head");
        if (indexOf != -1) {

            int endIndexOf = tmpScript.indexOf(">", indexOf);
            String startScript = tmpScript.substring(0, endIndexOf + 1);
            String endScript = tmpScript.substring(endIndexOf + 2);
            return startScript + "\n<script type=\"text/javascript\">\n" + JS_SHIM + "\n</script>\n" + endScript;
        } else {
            indexOf = tmpScript.indexOf("<html");
            if (indexOf != -1) {
                int endIndexOf = tmpScript.indexOf(">", indexOf);
                String startScript = tmpScript.substring(0, endIndexOf + 1);
                String endScript = tmpScript.substring(endIndexOf + 2);
                return startScript + "\n<head>\n<script type=\"text/javascript\">\n" + JS_SHIM + "\n</script>\n</head>\n" + endScript;
            } else {
                throw new IllegalArgumentException("Invalid script");
            }
        }
    }
}
