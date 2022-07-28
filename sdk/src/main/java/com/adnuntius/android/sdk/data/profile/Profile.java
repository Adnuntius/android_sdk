/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.adnuntius.android.sdk.StringUtils;
import com.adnuntius.android.sdk.data.DataRequest;
import com.adnuntius.android.sdk.data.DataTarget;

import static com.adnuntius.android.sdk.data.profile.FieldDataType.Timestamp;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.Date;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.String;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.Long;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Profile extends DataRequest {
    private String externalSystemType;
    private String externalSystemUserId;
    private Map<String, Object> profileValues;

    public Profile() {
        // for historical reasons, the '/visitor' endpoint for data is the profile update action
        super(DataTarget.visitor);
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

    /**
     * Set a 'Text' profile value
     *
     * @param key The profile key
     * @param value The 'Text' profile value
     */
    public void setProfileValue(final ProfileField key, final String value) {
        validateType(key, String);

        doSetProfileValue(key, value);
    }

    /**
     * Set a 'Integer' profile value
     *
     * The docs reference it as a 'Integer', even though as far as java is concerned its a Long
     *
     * @param key The profile key, must be a predefined key or a field mapping
     * @param value The 'Integer' profile value
     */
    public void setProfileValue(final ProfileField key, final Long value) {
        validateType(key, Long);

        doSetProfileValue(key, value);
    }

    /**
     * Set a 'Date' profile value
     *
     * @param key The profile key
     * @param value The 'Date' profile value
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public void setProfileValue(final ProfileField key, final java.time.LocalDate value) {
        validateType(key, Date);
        doSetProfileValue(key, value.toString());
    }

    public void setProfileValue(final ProfileField key, final com.adnuntius.android.sdk.data.profile.LocalDate value) {
        validateType(key, Date);
        doSetProfileValue(key, value.toString());
    }

    /**
     * Set a 'Timestamp' profile value
     *
     * @param key The profile key, must be a predefined key or a field mapping
     * @param value The 'Timestamp' profile value
     */
    public void setProfileValue(final ProfileField key, final com.adnuntius.android.sdk.data.profile.Instant value) {
        validateType(key, Timestamp);
        doSetProfileValue(key, value.toString());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public void setProfileValue(final ProfileField key, final java.time.Instant value) {
        validateType(key, Timestamp);
        doSetProfileValue(key, value.toString());
    }

    private void validateType(final ProfileField key, FieldDataType type) {
        if (!key.getDataType().equals(type)) {
            throw new IllegalArgumentException("Field Data Type mismatch");
        }
    }

    private void doSetProfileValue(final ProfileField key, final Object value) {
        StringUtils.validateNotBlank(key);
        StringUtils.validateNotBlank(value);

        if (profileValues == null) {
            profileValues = new HashMap<>();
        }
        profileValues.put(key.name(), value);
    }

    /**
     * Bypass the ProfileField validations to populate fields directly
     *
     * <pre>
     *     profile.getProfileValues().put("my profile value key", "my profile value");
     * </pre>
     */
    public Map<String, Object> getProfileValues() {
        if (this.profileValues == null) {
            return Collections.emptyMap();
        }
        return profileValues;
    }

    public String getExternalSystemType() {
        return externalSystemType;
    }

    public String getExternalSystemUserId() {
        return externalSystemUserId;
    }
}
