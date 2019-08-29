package com.adnuntius.android.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdConfig {
    private final String auId;
    private String auW;
    private String auH;

    @SerializedName("kv")
    private Map<String, List<String>> kvs;

    @SerializedName("c")
    private List<String> categories;

    public AdConfig(final String auId) {
        this.auId = auId;
    }

    public AdConfig setWidth(String auW) {
        this.auW = auW;
        return this;
    }

    public AdConfig setHeight(String auH) {
        this.auH = auH;
        return this;
    }

    /*
    Convenience method for pixel width
     */
    public AdConfig setWidth(int auW) {
        return(setHeight(auW + ""));
    }

    /*
    Convenience method for pixel height
     */
    public AdConfig setHeight(int auH) {
        return(setHeight(auH + ""));
    }

    public AdConfig addKeyValue(String key, String value) {
        if (kvs == null) {
            kvs = new HashMap<>();
        }
        List<String> values = kvs.get(key);
        if (values == null) {
            values = new ArrayList<>();
            kvs.put(key, values);
        }
        values.add(value);

        return this;
    }

    public AdConfig addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(category);
        return this;
    }

    public String toScript() {
        Gson gson = new GsonBuilder().create();
        String objJson = gson.toJson(this).replace('"', '\'');

        final String message = "<html>\n" +
                "   <head />\n" +
                "   <body>\n" +
                "       <div id=\"adn-" + auId + "\" style=\"display:none\"></div>\n" +
                "       <script type=\"text/javascript\">(function(d, s, e, t) { e = d.createElement(s); e.type = 'text/java' + s; e.async = 'async'; e.src = 'http' + ('https:' === location.protocol ? 's' : '') + '://cdn.adnuntius.com/adn.js'; t = d.getElementsByTagName(s)[0]; t.parentNode.insertBefore(e, t); })(document, 'script');window.adn = window.adn || {}; adn.calls = adn.calls || []; adn.calls.push(function() { \n" +
                "           adn.request({ sdk: 'android:" + BuildConfig.VERSION_NAME + "' adUnits: [ \n" +
                "               " + objJson + "\n" +
                "           ]});\n" +
                "        });\n" +
                "        </script>\n" +
                "   </body>\n" +
                "</html>";
        return message;
    }
}
