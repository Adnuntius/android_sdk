# Adnuntius SDK

Adnuntius Android SDK for Android enables embedded Ads and Adnuntius Data integration.

## Gradle Dependency

```
repositories {
        google()
        mavenCentral()

        // use a snapshot of the android-sdk
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }
```

Add a dependency to your build.gradle file:

```
implementation 'com.adnuntius.android.sdk:1.4.0-SNAPSHOT'
```

## Ad Delivery

Embed ads into your app using the AdnuntiusAdWebView.

### Add the AdnuntiusAdWebView to your xml layout file:

```xml
<com.adnuntius.android.sdk.AdnuntiusAdWebView
        android:id="@+id/adView"
        android:layout_width="354dp"
        android:layout_height="244dp"
        android:layout_alignBottom="@+id/swipeRefreshLayout"
        android:layout_centerHorizontal="true"
        tools:ignore="MissingConstraints" />
```

### Load Ad

In the Activity class load the web view in the onCreate (after calling setContentView), and then 
load the ad in the onResume, this will ensure that ads are reloaded when the app is paused and resumed.

The loadFromConfig accepts a CompletionHandler.  This completion handler callback
will be called asynchronously when the ad is either loaded into the webview or not, or when an error occurs.

A basic api for simple ad integrations, uses adn.js internally to render the ad and fire off all the right events.

```java
    @Override
    protected void onResume() {
        super.onResume();
        AdConfig config = new AdConfig("000000000006f450")
                .setWidth(300)
                .setHeight(200)
                .addKeyValue("version", "4.3");

        adView.loadFromConfig(config,
            new CompletionHandler() {
                @Override
                public void onComplete(int adCount) {
                    if (adCount == 0) {
                        // do something where no ad matches
                    }
                }
            
                @Override
                public void onFailure(String error) {
                   // do something on failure
                }
            });
    }
```

#### Limitations

The AdConfig class supports specifying a single ad unit, key values and categories only.

#### Gotchas

Due to the nature of the Android webview implementation its possible to receive an onComplete() with an adCount > 0, but then
receive an onFailure, in this case its most likely a configuration issue (your DIV id might be wrong for instance)

### Examples

An example app is available here: https://github.com/Adnuntius/android_sdk_examples

## Adnuntius Data

A new DataClient is available to generate sync, profile update (aka visitor) and page events to adnuntius data.

Refer to https://docs.adnuntius.com/adnuntius-data/api-documentation/http

```java
import com.adnuntius.android.sdk.data.DataClient;

...

final DataClient dataClient = new DataClient(getApplicationContext());
```

### Sync Profile

```java
final Sync sync = new Sync();
sync.setFolderId("000000000000009d");
sync.setBrowserId("some browser id");
sync.setExternalSystemIdentifier("SOME CRM SYSTEM", "some user id in crm SOME CRM SYSTEM");

dataClient.sync(sync, new DataResponseHandler() {
    @Override
    public void onSuccess() {
        // do something on success if necessary
    }

    @Override
    public void onFailure(ErrorResponse response) {
        // on failure, do something here
    }
});
```

### Profile Update

#### Date and Timestamp API 25 gotchas

If your application minSdkVersion is at least 26, you can use the setters which accept java.time.LocalDate or
java.time.Instant.

If your application minSdkVersion is before 26, you can use the setters which accept com.adnuntius.android.sdk.data.profile.LocalDate or
com.adnuntius.android.sdk.data.profile.Instant compatibility classes.

```java
final Profile profile = new Profile();
profile.setFolderId("some folder id");
profile.setBrowserId("some browser id");
profile.setExternalSystemIdentifier("SOME CRM SYSTEM", "some user id in crm SOME CRM SYSTEM");
profile.setProfileValue(ProfileFields.company, "Adnuntius");
profile.setProfileValue(ProfileFields.country, "Norway");
profile.setProfileValue(ProfileFields.dateOfBirth, LocalDate.now());
profile.setProfileValue(ProfileFields.createdAt, Instant.now());

dataClient.profile(profile, new DataResponseHandler() {
    @Override
    public void onSuccess() {
        // do something on success if necessary
    }

    @Override
    public void onFailure(ErrorResponse response) {
        // on failure, do something here
    }
});
```

### Page View

If you use the constructor which accepts a page url, the domainName and url path categories
will be derived.   If you want to handle this yourself, use the setDomainName and addCategories
methods directly.

```java
final Page page = new Page("the page url");
page.setBrowserId("some browser id");
page.setFolderId("some folder id");
page.addCategories("minecraft", "doom");
page.addKeywords("wood", "plastic");
dataClient.page(page, new DataResponseHandler() {
    @Override
    public void onSuccess() {
        // do something on success if necessary
    }

    @Override
    public void onFailure(ErrorResponse response) {
        // on failure, do something here
    }
});
```

## Bugs, Issues and Support

This SDK is a work in progress and will be given attention when necessary based on feed back.  You
can raise issues on github or via zen desk at https://admin.adnuntius.com

# License

This project uses the Apache 2 License.  Refer to the LICENSE file.

