package com.adnuntius.android.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ad request used by AdnuntiusAdWebView loadAd method
 */
public class AdRequest {
    private final String auId;
    private String auW;
    private String auH;

    private transient boolean noCookies;

    @SerializedName("kv")
    private Map<String, List<String>> kvs;

    @SerializedName("c")
    private List<String> categories;

    public AdRequest(final String auId) {
        this.auId = auId;
    }

    public String getAuId() {
        return auId;
    }

    public AdRequest setWidth(String auW) {
        this.auW = auW;
        return this;
    }

    public AdRequest setHeight(String auH) {
        this.auH = auH;
        return this;
    }

    public AdRequest noCookies() {
        this.noCookies = true;
        return this;
    }

    public boolean useCookies() {
        // noCookies: true == useCookies false
        // confusing I know, but noCookies is adn.js
        // and useCookies is ad server parameter
        return !this.noCookies;
    }

    /*
    Convenience method for width
     */
    public AdRequest setWidth(int auW) {
        return(setWidth(auW + ""));
    }

    /*
    Convenience method for height
     */
    public AdRequest setHeight(int auH) {
        return(setHeight(auH + ""));
    }

    public AdRequest addKeyValue(String key, String value) {
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

    @Deprecated
    public AdRequest addCategory(String category) {
        return addCategories(category);
    }

    public AdRequest addCategories(String ... categories) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        Collections.addAll(this.categories, categories);
        return this;
    }
}
