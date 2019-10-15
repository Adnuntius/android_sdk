## 1.2.0

Significant API changes were made to the SDK for version 1.2.0   

The AdView class was replaced with AdnuntiusAdWebView
The method loadAdFromScript was replaced with loadFromScript
The method loadAdFromJson was replaced with loadFromApi
A new method was added called loadFromConfig.

All of these methods have a CompletionHandler which you can use to adjust your UI depending on whether ads have been shown or 
not.

## 1.1.5

Added JCenter deployments so that packaging an App with the SDK can be done without having to manually embed the SDK

Enabled capability to load more than one ad configuration, as previous versions uses a static configuration object
which made this impossible
