package com.adnuntius.android.sdk.data;

import android.os.Build;

import com.adnuntius.android.sdk.AdnuntiusEnvironment;
import com.adnuntius.android.sdk.BuildConfig;
import com.adnuntius.android.sdk.data.profile.Profile;
import com.adnuntius.android.sdk.data.profile.ProfileFields;
import com.adnuntius.android.sdk.http.HttpResponseHandler;
import com.adnuntius.android.sdk.http.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.Instant;
import java.time.LocalDate;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonNodePresent;
import static net.javacrumbs.jsonunit.JsonAssert.whenIgnoringPaths;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(sdk=Build.VERSION_CODES.N)
@RunWith(RobolectricTestRunner.class)
public class DataClientTest {
    private final Gson gson = new GsonBuilder().create();

    @Test
    public void testPageRequest() {
        final Page page = new Page();
        page.setBrowserId("123123123");
        page.setFolderId("folae213123");
        page.setDomainName("adnuntius.com");
        page.addCategories("minecraft", "doom");
        page.addKeywords("wood", "pla");
        testRequest(page);
    }

    @Test
    public void testSyncRequest() {
        final Sync sync = new Sync();
        sync.setBrowserId("bradadasd");
        sync.setExternalSystemIdentifier("SOME SYSTEM", "adsdasdasd");
        sync.setFolderId("adssadasdad");
        testRequest(sync);
    }

    @Test
    public void testProfileUpdateRequest() {
        final Profile profile = new Profile();
        profile.setExternalSystemIdentifier("asdasd", "asdasdasd");
        profile.setFolderId("asdasdasdas");
        profile.setBrowserId("adasd21312321");
        profile.setProfileValue(ProfileFields.company, "Adnuntius");
        profile.setProfileValue(ProfileFields.country, "Norway");
        profile.setProfileValue(ProfileFields.dateOfBirth, LocalDate.now());
        profile.setProfileValue(ProfileFields.createdAt, Instant.now());
        testRequest(profile);
    }

    private void testRequest(final DataRequest request) {
        final HttpClient httpClient = mock(HttpClient.class);
        final DataClient client = new DataClient(AdnuntiusEnvironment.dev, httpClient);

        final DataResponseHandler handler = mock(DataResponseHandler.class);
        ArgumentCaptor<String> urlCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(httpClient).postJsonRequest(urlCapture.capture(), jsonCapture.capture(), any());

        if (request instanceof Page) {
            client.page((Page) request, handler);
        } else if (request instanceof Sync) {
            client.sync((Sync) request, handler);
        } else if (request instanceof Profile) {
            client.profile((Profile) request, handler);
        }

        verify(httpClient).postJsonRequest(any(String.class), any(String.class), any(HttpResponseHandler.class));
        final String url = urlCapture.getValue();
        assertTrue(url.contains("/" + request.getTarget() + "?sdk=android:" + BuildConfig.VERSION_NAME));

        final String jsonString = jsonCapture.getValue();
        assertJsonNodePresent(
                jsonString,
                "occurredAt"
        );

        assertJsonNodePresent(
                jsonString,
                "userTimezone"
        );

        assertJsonEquals(
                gson.toJson(request),
                jsonString,
                whenIgnoringPaths("occurredAt", "userTimezone")
        );
    }
}
