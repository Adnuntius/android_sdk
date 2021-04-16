package com.adnuntius.android.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
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

    public String getAuId() {
        return auId;
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
    Convenience method for width
     */
    public AdConfig setWidth(int auW) {
        return(setWidth(auW + ""));
    }

    /*
    Convenience method for height
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

    @Deprecated
    public AdConfig addCategory(String category) {
        return addCategories(category);
    }

    public AdConfig addCategories(String ... categories) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        Collections.addAll(this.categories, categories);
        return this;
    }
}
