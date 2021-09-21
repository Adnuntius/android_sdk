package com.adnuntius.android.sdk;

import androidx.annotation.Nullable;

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
    private transient String userId;
    private transient String sessionId;
    private transient boolean useCookies;
    private transient LivePreview livePreview;

    @SerializedName("kv")
    private Map<String, List<String>> kvs;

    @SerializedName("c")
    private List<String> categories;

    public AdRequest(final String auId) {
        this.auId = auId;
    }

    public String auId() {
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

    @Deprecated
    public AdRequest noCookies() {
        return useCookies(false);
    }

    public AdRequest useCookies(final boolean useCookies) {
        this.useCookies = useCookies;
        return this;
    }

    public AdRequest userId(final String userId) {
        this.userId = userId;
        return this;
    }

    public AdRequest sessionId(final String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public AdRequest livePreview(final String lpl, @Nullable final String lpc) {
        this.livePreview = new LivePreview(lpl, lpc);
        return this;
    }

    LivePreview livePreview() {
        return this.livePreview;
    }

    String userId() {
        return this.userId;
    }

    String sessionId() {
        return this.sessionId;
    }

    boolean useCookies() {
        return this.useCookies;
    }

    /*
    Convenience method for width
     */
    public AdRequest setWidth(final int auW) {
        return(setWidth(auW + ""));
    }

    /*
    Convenience method for height
     */
    public AdRequest setHeight(final int auH) {
        return(setHeight(auH + ""));
    }

    public AdRequest addKeyValue(final String key, final String value) {
        if (this.kvs == null) {
            this.kvs = new HashMap<>();
        }
        List<String> values = this.kvs.get(key);
        if (values == null) {
            values = new ArrayList<>();
            this.kvs.put(key, values);
        }
        values.add(value);

        return this;
    }

    @Deprecated
    public AdRequest addCategory(final String category) {
        return addCategories(category);
    }

    public AdRequest addCategories(final String ... categories) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        Collections.addAll(this.categories, categories);
        return this;
    }

    List<String> getCategories() {
        return this.categories;
    }

    Map<String, List<String>> getKeyValues() {
        return this.kvs;
    }

    String getHeight() {
        return this.auH;
    }

    String getWidth() {
        return this.auW;
    }
}
