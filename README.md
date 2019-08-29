# Adnuntius Android SDK

Adnuntius Android SDK is an android sdk which allows business partners to embed Adnuntius ads in their native android applications.

## Integration

Add a dependency to your build.gradle file:

```
compile 'com.adnuntius.android.sdk:1.1.4'
```


- In `MainActivity` inside method `onResume`:
```java
    private AdView webView;

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

       webView.loadAdFromScript(adScript);
    }
```
- Add implementation inside your xml layout file:
```xml
<com.adnuntius.android.sdk.AdView
        android:id="@+id/adView"
        android:layout_width="354dp"
        android:layout_height="244dp"
        android:layout_alignBottom="@+id/swipeRefreshLayout"
        android:layout_centerHorizontal="true"
        tools:ignore="MissingConstraints" />
```

## Examples

Some examples of using the SDK are available from https://github.com/Adnuntius/android_sdk_examples
