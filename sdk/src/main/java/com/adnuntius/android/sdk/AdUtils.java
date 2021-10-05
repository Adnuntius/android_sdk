package com.adnuntius.android.sdk;

import java.util.stream.Collectors;

final class AdUtils {
    public static class AdResponse {
        private final String html;
        private final int adCount;

        public AdResponse(final String html, final int adCount) {
            this.html = html;
            this.adCount = adCount;
        }

        public String getHtml() {
            return html;
        }

        public int getAdCount() {
            return adCount;
        }
    }

    private static final String JS_SHIM =
        "var adnSdkShim = new Object();\n" +
        "adnSdkShim.reallyOpen = XMLHttpRequest.prototype.open;\n" +
        "adnSdkShim.reallySend = XMLHttpRequest.prototype.send;\n" +
        "\n" +
        "adnSdkShim.ajaxEvent = function(url, status, response) {\n" +
        "   if (status == 200) {\n" +
        "       var adCount = this.getAdsCount(response)\n" +
    "           intAndroidAdnuntius.onComplete(url, adCount);\n" +
        "   } else {\n" +
        "       intAndroidAdnuntius.onFailure(url, status);\n" +
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

    private AdUtils() {
    }

    static String getAdScript(final AdRequest request, final String adUnitsJson) {
        final String globalProperties = request.globalProperties().entrySet().stream().map(e -> "                   " + e.getKey() + ": \"" + e.getValue() + "\"")
                .collect(Collectors.joining(",\n"));

        return "<html>\n" +
                "<head>\n" +
                "   <script type=\"text/javascript\">\n" + JS_SHIM + "\n</script>\n" +
                "   <script type=\"text/javascript\" src=\"https://cdn.adnuntius.com/adn.js\" async></script>\n" +
                "</head>\n" +
                "   <body>\n" +
                "       <div id=\"adn-" + request.auId() + "\" style=\"display:none\"></div>\n" +
                "       <script type=\"text/javascript\">\n" +
                "           window.adn = window.adn || {}; adn.calls = adn.calls || [];\n" +
                "           adn.calls.push(function() {\n" +
                "               adn.request({\n" +
                (request.useCookies() ? "" : "                   useCookies: false,\n")+
                (globalProperties.isEmpty() ? "" : globalProperties + ",\n") +
                "                   adUnits: [" + adUnitsJson + "]\n" +
                "               });\n" +
                "           });\n" +
                "       </script>" +
                "   </body>\n" +
                "</html>";
    }
}
