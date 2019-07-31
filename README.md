# Adnuntius Android SDK

Adnuntius Android SDK is an android sdk which allows business partners to embed Adnuntius ads in their native android applications.

## Integration

- Add module `lib-release.aar` or `lib-debug.aar` to your project
- In `MainActivity` inside method `OnCreate` inject Adnuntius config script
```java
AdView.adScript = "<html>\n" +
                "        <head />\n" +
                "        <body>\n" +
                "        <div id=\"adn-0000000000000fe6\" style=\"display:none\"></div>\n" +
                "        <script type=\"text/javascript\">(function(d, s, e, t) { e = d.createElement(s); e.type = 'text/java' + s; e.async = 'async'; e.src = 'http' + ('https:' === location.protocol ? 's' : '') + '://cdn.adnuntius.com/adn.js'; t = d.getElementsByTagName(s)[0]; t.parentNode.insertBefore(e, t); })(document, 'script');window.adn = window.adn || {}; adn.calls = adn.calls || []; adn.calls.push(function() { adn.request({ adUnits: [ {auId: '0000000000000fe6', auW: 320, auH: 480 } ]}); });</script>\n" +
                "        </body>\n" +
                "        </html>";
```
- Add implementation inside your xml layout file:
```xml
<com.adnuntius.sdk.lib.AdView
        android:id="@+id/facebookWebview"
        android:layout_width="354dp"
        android:layout_height="244dp"
        android:layout_alignBottom="@+id/swipeRefreshLayout"
        android:layout_centerHorizontal="true"
        tools:ignore="MissingConstraints" />
```

- To update SDK copy and paste newer version inside lib-release or lib-debug folder in your project

## Examples

An example of using the SDK is available from https://github.com/Adnuntius/android_sdk_examples


