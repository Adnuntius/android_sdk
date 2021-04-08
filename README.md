# Adnuntius Android SDK

Adnuntius Android SDK is an android sdk which allows business partners to embed Adnuntius ads in their native android applications.

## Gradle Dependency

```
repositories {
        google()
        mavenCentral()
    }
```

Add a dependency to your build.gradle file:

```
implementation 'com.adnuntius.android.sdk:1.3.0'
```

## Add the AdnuntiusAdWebView to your xml layout file:

```xml
<com.adnuntius.android.sdk.AdnuntiusAdWebView
        android:id="@+id/adView"
        android:layout_width="354dp"
        android:layout_height="244dp"
        android:layout_alignBottom="@+id/swipeRefreshLayout"
        android:layout_centerHorizontal="true"
        tools:ignore="MissingConstraints" />
```

## Load Ad

In the Activity class load the web view in the onCreate (after calling setContentView), and then 
load the ad in the onResume, this will ensure that ads are reloaded when the app is paused and resumed.

The loadFromConfig and loadFromApi both accept a CompletionHandler.  This completion handler callback
will be called asynchronously when the ad is either loaded into the webview or not, or when an error occurs.  Due to the
nature of the Android webview implementation its possible to receive an onComplete() with an adCount > 0, but then
receive a onFailure, in this case its most likely a configuration issue (your DIV id might be wrong for instance)

### Load From Config

A very basic api for simple ad integrations, internally utilises adn.js

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


### Load From Api

Skip adn.js and load the ad html direct from the ad server.

```java
    @Override
    protected void onResume() {
        super.onResume();
        
        adView.loadFromApi("{\"adUnits\": [{\"auId\": \"000000000006f450\", \"kv\": [{\"version\":\"10\"}]}]}",
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


## Examples

An example app which loads ads via both load methods is available here: https://github.com/Adnuntius/android_sdk_examples


## Bugs, Issues and Support

This SDK is a work in progress and will be given attention when necessary based on feed back from business partners.  You
can raise issues on github or via zendesk at https://admin.adnuntius.com
