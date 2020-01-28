# Adnuntius Android SDK

Adnuntius Android SDK is an android sdk which allows business partners to embed Adnuntius ads in their native android applications.

## Gradle Dependency

Our latest sdk is deployed to jcenter() only, so you need to ensure you include jcenter() in your repositories list:

```
repositories {
        mavenCentral()
        google()
        jcenter()
    }
```

Add a dependency to your build.gradle file:

```
implementation 'com.adnuntius.android.sdk:1.2.2'
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

The loadFromScript, loadFromConfig and loadFromApi all accept a CompletionHandler.  This completion handler callback
will be called asynchronously when the ad is either loaded into the webview or not, or when an error occurs.  Due to the
nature of the Android webview implementation its possible to receive an onComplete() with an adCount > 0, but then
receive a onFailure, in this case its most likely a configuration issue (your DIV id might be wrong for instance)

### Load From Script

Directly reference adn.js and provide a complete html page, maximum flexibility

```java
    private AduntiusAdWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.webView = findViewById(R.id.adView);
    }

    @Override
    protected void onResume() {
       super.onResume();
       String adScript = "<html>\n" +
                        "        <head />\n" +
                        "        <body>\n" +
                        "        <div id=\"adn-0000000000000fe6\" style=\"display:none\"></div>\n" +
                        "        <script type=\"text/javascript\">(function(d, s, e, t) { e = d.createElement(s); e.type = 'text/java' + s; e.async = 'async'; e.src = 'http' + ('https:' === location.protocol ? 's' : '') + '://cdn.adnuntius.com/adn.js'; t = d.getElementsByTagName(s)[0]; t.parentNode.insertBefore(e, t); })(document, 'script');window.adn = window.adn || {}; adn.calls = adn.calls || []; adn.calls.push(function() { adn.request({ adUnits: [ {auId: '0000000000000fe6', auW: 320, auH: 480 } ]}); });</script>\n" +
                        "        </body>\n" +
                        "        </html>";

       webView.loadAdFromScript(adScript,
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

An example app which loads ads via all 3 load methods is available here: https://github.com/Adnuntius/android_sdk_examples/example


## Bugs, Issues and Support

This SDK is a work in progress and will be given attention when necessary based on feed back from business partners.  You
can raise issues on github or via zendesk at https://admin.adnuntius.com

## Releasing

Releases are uploaded to bintray and onto jcenter using the bintray gradle plugin.

