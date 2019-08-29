# Adnuntius Android SDK

Adnuntius Android SDK is an android sdk which allows business partners to embed Adnuntius ads in their native android applications.

## Integration

Add a dependency to your build.gradle file:

```
compile 'com.adnuntius.android.sdk:1.1.5-SNAPSHOT'
```

In the Activity class load the web view in the onCreate (after calling setContentView), and then 
load the ad in the onResume, this will ensure that ads are reloaded when the app is paused and resumed:

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
       
       AdConfig config = new AdConfig("0000000000023ae5")
           .setHeight(webView.getHeight())
           .setWidth(webView.getWidth())
           .addKeyValue("car", "toyota")
           .addKeyValue("car", "ford")
           .addKeyValue("sport", "football")
           .addCategory("sports")
           .addCategory("casinos");
       
       webView.loadForConfig(config);
    }
```

Add adview inside your xml layout file:

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
