package com.adnuntius.android.sdk.data.profile;

public interface ProfileField {
    FieldDataType getDataType();
    FieldGroup getGroup();
    String name();
}
