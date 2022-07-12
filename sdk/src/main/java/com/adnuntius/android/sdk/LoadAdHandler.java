package com.adnuntius.android.sdk;

public interface LoadAdHandler {
    class AdResponseInfo {
        public int definedWidth;
        public int definedHeight;
        public int width;
        public int height;
        public String creativeId;
        public String lineItemId;
    }

    /*
     If adnuntius ad server returns a non 200 status, or there is no target div
     or adn.js reports any other issue
    */
    default void onFailure(String message) {
    }

    /*
     No ads was returned
     */
    default void onNoAdResponse() {
    }

    /*
     This will not be called if there is no ad rendered (should be obvious)
    */
    void onAdResponse(final AdResponseInfo info);

    /*
     Pass through the onRestyle event from adn.js, this is currently
     enabled experimentally and for debugging purposes only
     */
    default void onAdResize(final AdResponseInfo info) {
    }

    /*
     Used for close view from layout
     https://github.com/Adnuntius/ios_sdk/wiki/Adnuntius-Advertising#close-view-from-layout
     */
    default void onLayoutCloseView() {
    }
}
