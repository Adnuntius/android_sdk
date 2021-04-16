package com.adnuntius.android.sdk.http.volley;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.data.DataClient;
import com.adnuntius.android.sdk.data.DataResponseHandler;
import com.adnuntius.android.sdk.data.Page;
import com.adnuntius.android.sdk.data.profile.Profile;
import com.adnuntius.android.sdk.data.Sync;
import com.adnuntius.android.sdk.data.profile.ProfileFields;
import com.adnuntius.android.sdk.data.profile.ProfileResponseHandler;
import com.adnuntius.android.sdk.http.AuthClient;
import com.adnuntius.android.sdk.http.AuthenticateResponseHandler;
import com.adnuntius.android.sdk.http.BearerToken;
import com.adnuntius.android.sdk.http.HttpClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class DataClientVolleyTest {
    private static final String devFolderId = "000000000000009d";
    private static final String devBrowserId = "23123123132123123213213";
    private static final String devDataUsername = "jason+sdk@adnuntius.com";
    private static final String devDataPassword = "";

    private HttpClient httpClient;
    private TestHttpRequestQueue queue;
    private DataClient dataClient;
    private AuthClient authClient;

    @Before
    public void setUp() {
        queue = new TestHttpRequestQueue();
        httpClient = new VolleyHttpClient(queue);
        dataClient = new DataClient(AdnuntiusEnvironment.dev, httpClient);
        authClient = new AuthClient(AdnuntiusEnvironment.dev, httpClient);
    }

    @Test
    public void testGetProfile() {
        final Profile profile = new Profile();
        profile.setExternalSystemIdentifier("hello", "world");
        profile.setFolderId(devFolderId);
        profile.setBrowserId(devBrowserId);
        profile.setProfileValue(ProfileFields.company, "Adnuntius");
        profile.setProfileValue(ProfileFields.country, "Norway");
        profile.setProfileValue(ProfileFields.dateOfBirth, LocalDate.now());
        profile.setProfileValue(ProfileFields.createdAt, Instant.now());

        final DataResponseHandler handler = mock(DataResponseHandler.class);
        dataClient.profile(profile, handler);

        queue.waitForMessages();
        verify(handler).onSuccess();

        final AuthenticateResponseHandler authHandler = mock(AuthenticateResponseHandler.class);
        ArgumentCaptor<BearerToken> response = ArgumentCaptor.forClass(BearerToken.class);
        doNothing().when(authHandler).onSuccess(response.capture());

        authClient.authenticate(devDataUsername, devDataPassword, authHandler);
        queue.waitForMessages();

        final BearerToken token = response.getValue();
        assertNotNull(token);

        final ProfileResponseHandler profileHandler = mock(ProfileResponseHandler.class);
        ArgumentCaptor<Profile> profileResponse = ArgumentCaptor.forClass(Profile.class);
        doNothing().when(profileHandler).onSuccess(profileResponse.capture());
        dataClient.getProfile(devFolderId, devBrowserId, token, profileHandler);
        queue.waitForMessages();
        final Profile savedProfile = profileResponse.getValue();
        assertNotNull(savedProfile);
    }

    @Test
    public void testAuthRequest() {
        final AuthenticateResponseHandler handler = mock(AuthenticateResponseHandler.class);
        ArgumentCaptor<BearerToken> response = ArgumentCaptor.forClass(BearerToken.class);
        doNothing().when(handler).onSuccess(response.capture());

        authClient.authenticate(devDataUsername, devDataPassword, handler);
        queue.waitForMessages();

        final BearerToken token = response.getValue();
        assertNotNull(token);

        doNothing().when(handler).onSuccess(response.capture());
        authClient.refresh(token, handler);
        queue.waitForMessages();

        final BearerToken token2 = response.getValue();
        assertNotNull(token);
        assertNotEquals(token.getAccessToken(), token2.getAccessToken());
    }

    @Test
    public void testRealDataSync() {
        final Sync sync = new Sync();
        sync.setBrowserId(devBrowserId);
        sync.setFolderId(devFolderId);
        sync.setExternalSystemIdentifier("SOME SYSTEM", "adsdasdasd");

        final DataResponseHandler handler = mock(DataResponseHandler.class);
        dataClient.sync(sync, handler);

        queue.waitForMessages();
        verify(handler).onSuccess();
    }

    @Test
    public void testRealPage() {
        final Page page = new Page();
        page.setBrowserId(devBrowserId);
        page.setFolderId(devFolderId);
        page.setDomainName("adnuntius.com");
        page.addCategories("minecraft", "doom");
        page.addKeywords("wood", "pla");

        final DataResponseHandler handler = mock(DataResponseHandler.class);
        dataClient.page(page, handler);

        queue.waitForMessages();
        verify(handler).onSuccess();
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

        final DataResponseHandler handler = mock(DataResponseHandler.class);
        dataClient.profile(profile, handler);

        queue.waitForMessages();
        verify(handler).onSuccess();
    }
}
