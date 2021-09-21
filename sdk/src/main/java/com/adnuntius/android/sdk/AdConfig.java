package com.adnuntius.android.sdk;

/**
 * @see AdRequest
 */
@Deprecated
public class AdConfig extends AdRequest {
    public AdConfig(String auId) {
        super(auId);
    }

    @Deprecated
    public AdConfig setWidth(String auW) {
        super.setWidth(auW);
        return this;
    }

    @Deprecated
    public AdConfig setHeight(String auH) {
        super.setHeight(auH);
        return this;
    }

    @Deprecated
    public AdConfig setWidth(int auW) {
        return(setWidth(auW + ""));
    }

    @Deprecated
    public AdConfig setHeight(int auH) {
        super.setHeight(auH);
        return this;
    }

    @Deprecated
    public AdConfig addKeyValue(String key, String value) {
        super.addKeyValue(key, value);
        return this;
    }

    @Deprecated
    public AdConfig addCategory(String category) {
        super.addCategories(category);
        return this;
    }

    @Deprecated
    public AdConfig addCategories(String ... categories) {
        super.addCategories(categories);
        return this;
    }
}
