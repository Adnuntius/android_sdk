/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data.profile;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testJdkDateAndTimeSetters() {
        final Profile profile = new Profile();
        profile.setExternalSystemIdentifier("asdasd","asdasdasd");
        profile.setFolderId("asdasdasdas");
        profile.setBrowserId("adasd21312321");
        profile.setProfileValue(ProfileFields.company, "Adnuntius");
        profile.setProfileValue(ProfileFields.country, "Norway");
        profile.setProfileValue(ProfileFields.dateOfBirth, java.time.LocalDate.now());
        profile.setProfileValue(ProfileFields.createdAt, java.time.Instant.now());
    }

    @Test
    public void testCompatDateAndTimeSetters() {
        final Profile profile = new Profile();
        profile.setExternalSystemIdentifier("asdasd","asdasdasd");
        profile.setFolderId("asdasdasdas");
        profile.setBrowserId("adasd21312321");
        profile.setProfileValue(ProfileFields.company, "Adnuntius");
        profile.setProfileValue(ProfileFields.country, "Norway");
        profile.setProfileValue(ProfileFields.dateOfBirth, LocalDate.now());
        profile.setProfileValue(ProfileFields.createdAt, Instant.now());
    }
}
