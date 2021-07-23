# System Events

## Required events
Our SDK only needs to send one event by default. That is triggered when SDK loads.

#### SDK start
`sdk_start` event is triggered as soon as initialization function is called via [`initializeAnalyticsEngine`](/api.md#init) api. This allows us to record a user.


## Optional events
We support a lot more system events which you can enable via Manifest in your dashboard. Navigate to [https://[your-dashboard-url]/application/manifest]() and select application that which you are working with. In the list look for `SDK_System_Events` where you can select all events that you want.

List of optional system events:
- Application Installed
- Application Updated
- Deep Link Opened
- App Opened
- App In Background
- App In Foreground
- App Session Info
