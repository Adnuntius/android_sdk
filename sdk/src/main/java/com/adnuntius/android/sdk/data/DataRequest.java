/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data;

import com.adnuntius.android.sdk.StringUtils;

public abstract class DataRequest {
    private final transient DataTarget type;
    private String browserId;
    private String folderId;

    protected DataRequest(final DataTarget type) {
        this.type = type;
    }

    /**
     * A unique ID for the browser or device being used.
     *
     * @param browserId
     */
    public void setBrowserId(String browserId) {
        StringUtils.validateNotBlank(browserId);
        this.browserId = browserId;
    }

    /**
     * The Folder ID in Adnuntius Data
     *
     * @param folderId
     */
    public void setFolderId(String folderId) {
        StringUtils.validateNotBlank(folderId);
        this.folderId = folderId;
    }

    public String getFolderId() {
        return folderId;
    }

    public String getBrowserId() {
        return browserId;
    }

    public DataTarget getTarget() {
        return type;
    }
}
