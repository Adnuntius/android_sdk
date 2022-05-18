package com.adnuntius.android.sdk.http.volley;

import android.content.Context;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.data.DataClient;
import com.adnuntius.android.sdk.data.profile.Profile;
import com.adnuntius.android.sdk.data.profile.ProfileFields;
import com.adnuntius.android.sdk.http.HttpClient;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

/**
 * Run this on a android emulator, as robolectric + volley don't play nice with 404 errors
 */
public class DataClientAndroidVolleyTest {
    private static final String devFolderId = "000000000000009d";
    private static final String devBrowserId = "23123123132123123213213";

    private final MockHttpResponseHandler handler = new MockHttpResponseHandler();

    private DataClient dataClient;

    @Before
    public void setUp() {
        final HttpClient httpClient = new VolleyHttpClient((Context) getApplicationContext());
        dataClient = new DataClient(AdnuntiusEnvironment.dev, httpClient);
        handler.clear();
    }

    @Test
    public void testRealProfile() {
        final Profile profile = new Profile();
        profile.setExternalSystemIdentifier("asdasd", "asdasdasd");
        profile.setFolderId(devFolderId);
        profile.setBrowserId(devBrowserId);
        profile.setProfileValue(ProfileFields.company, "Adnuntius");
        profile.setProfileValue(ProfileFields.country, "Norway");
        profile.setProfileValue(ProfileFields.dateOfBirth, LocalDate.now());
        profile.setProfileValue(ProfileFields.createdAt, Instant.now());
        dataClient.profile(profile, handler);

        handler.waitForMessages(1);
        assertEquals(1, handler.responses.get());
    }
}
