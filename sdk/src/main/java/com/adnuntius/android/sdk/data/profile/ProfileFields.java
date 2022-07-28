/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import static com.adnuntius.android.sdk.data.profile.FieldDataType.Timestamp;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.Date;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.String;
import static com.adnuntius.android.sdk.data.profile.FieldDataType.Long;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.Basics;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.ContactInfo;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.Education;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.SocialMedia;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.Transactions;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.Products;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.InterestsAndHobbies;
import static com.adnuntius.android.sdk.data.profile.FieldGroup.ExtraFields;

public enum ProfileFields implements ProfileField {
    firstName(Basics),
    lastName(Basics),
    title(Basics),
    dateOfBirth(Date, Basics),
    age(Long, Basics),
    gender(Basics),
    status(Basics),
    language(Basics),
    description(Basics),
    company(Basics),
    website(Basics),
    rank(Basics),
    level(Basics),
    type(Basics),
    emailPrivate(ContactInfo),
    emailWork(ContactInfo),
    phone(ContactInfo),
    mobilePhone(ContactInfo),
    addressLine1(ContactInfo),
    addressLine2(ContactInfo),
    state(ContactInfo),
    city(ContactInfo),
    areaCode(ContactInfo),
    zipCode(ContactInfo),
    postCode(ContactInfo),
    region(ContactInfo),
    country(ContactInfo),
    educationName(Education),
    educationType(Education),
    educationDegree(Education),
    educationSchool(Education),
    educationField(Education),
    educationStartYear(Long, Education),
    educationEndYear(Long, Education),
    skill1Primary(Education),
    skill2(Education),
    skill3(Education),
    facebook(SocialMedia),
    instagram(SocialMedia),
    snapchat(SocialMedia),
    twitter(SocialMedia),
    avatar(SocialMedia),
    transactionAmount(Long, Transactions),
    transactionCurrency(Transactions),
    lastTransaction(Timestamp, Transactions),
    firstTransaction(Timestamp, Transactions),
    createdAt(Timestamp, Transactions),
    lastLogin(Timestamp, Transactions),
    firstInteraction(Timestamp, Transactions),
    lastInteraction(Timestamp, Transactions),
    logins(Long, Transactions),
    product1(Products),
    product2(Products),
    favouriteTopic1(InterestsAndHobbies),
    favouriteTopic2(InterestsAndHobbies),
    personalInterest1(InterestsAndHobbies),
    personalInterest2(InterestsAndHobbies),
    customString1(ExtraFields),
    customString2(ExtraFields),
    customString3(ExtraFields),
    customString4(ExtraFields),
    customString5(ExtraFields),
    customLong1(Long, ExtraFields),
    customLong2(Long, ExtraFields),
    customLong3(Long, ExtraFields),
    customLong4(Long, ExtraFields),
    customLong5(Long, ExtraFields);

    private final FieldDataType dataType;
    private final FieldGroup fieldGroup;

    ProfileFields(final FieldGroup fieldGroup) {
        this(String, fieldGroup);
    }

    ProfileFields(final FieldDataType dataType, final FieldGroup fieldGroup) {
        this.dataType = dataType;
        this.fieldGroup = fieldGroup;
    }

    @Override
    public FieldDataType getDataType() {
        return dataType;
    }

    @Override
    public FieldGroup getGroup() {
        return fieldGroup;
    }
}
