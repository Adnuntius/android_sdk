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
    private transient boolean useCookies;
    private transient Map<String, String> globalParameters;
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
        globalParameter(GlobalProperty.userId, userId);
        return this;
    }

    public AdRequest sessionId(final String sessionId) {
        return globalParameter(GlobalProperty.sessionId, sessionId);
    }

    public AdRequest consentString(final String sessionId) {
        return globalParameter(GlobalProperty.consentString, sessionId);
    }

    public AdRequest globalParameter(final GlobalProperty key, final String value) {
        return globalParameter(key.name(), value);
    }

    public AdRequest globalParameter(final String key, final String value) {
        if (globalParameters == null) {
            globalParameters = new HashMap<>();
        }
        globalParameters.put(key, value);
        return this;
    }

    public AdRequest livePreview(final String lpl, @Nullable final String lpc) {
        this.livePreview = new LivePreview(lpl, lpc);
        return this;
    }

    LivePreview livePreview() {
        return this.livePreview;
    }

    /**
     * yes this is the actual map, not a copy of a immutable wrapper
     */
    Map<String, String> globalProperties() {
        if (globalParameters == null) {
            return Collections.emptyMap();
        }
        return globalParameters;
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
