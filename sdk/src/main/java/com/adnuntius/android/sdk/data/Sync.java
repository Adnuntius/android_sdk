package com.adnuntius.android.sdk.data;

import com.adnuntius.android.sdk.StringUtils;

public class Sync extends DataRequest {
    private String externalSystemType;
    private String externalSystemUserId;

    public Sync() {
        super(DataTarget.sync);
    }

    /**
     * User external system type and identifier
     *
     * @param externalSystemType A unique identifier, e.g. CRM name, that corresponds to the external system providing the data
     * @param externalSystemUserId The unique identifier for the user in the external system
     */
    public void setExternalSystemIdentifier(
            final String externalSystemType, final String externalSystemUserId) {
        StringUtils.validateNotBlank(externalSystemType, externalSystemUserId);
        this.externalSystemType = externalSystemType;
        this.externalSystemUserId = externalSystemUserId;
    }

    public String getExternalSystemType() {
        return externalSystemType;
    }

    public String getExternalSystemUserId() {
        return externalSystemUserId;
    }
}
