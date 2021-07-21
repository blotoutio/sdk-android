# Basic API's

## init
The `init` method is used for initializing SDK. This sets all required configurations and also sends system event `sdk_start` which allows it to record user.
#### Input
`BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration);`

|||||
|---|---|---|---|
| `applicationContext` | `Object` | Application Context |
| `blotoutAnalyticsConfiguration` | `BlotoutAnalyticsConfiguration` | This Model contains information related to SDK initialization |

```
```
## BlotoutAnalyticsConfiguration

|||||
|---|---|---|---|
| `setBlotoutSDKKey` | `String` |  | Application token that you can get in your dashboard. |
| `setEndPointUrl` | `String` |  | Url where you will be sending data. |



#### Example
```kotlin
        BlotoutAnalyticsConfiguration blotoutAnalyticsConfiguration = new BlotoutAnalyticsConfiguration();
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("EADAH5FV8B5MMVZ");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://stage.blotout.io/sdk/");
        BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration);
```

## capture
The `capture` method is used to record developer events. This allows you to send custom events to the server when a user is interacting with the app. For example, one custom event would be when a user adds an item to a cart.
#### Input
`capture(eventName: String, eventInfo: HashMap<String, Any>)`

|||||
|---|---|---|---|
| `eventName` | `String` |  | Name of the event that you are sending |
| `eventInfo` | `Object` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### Example
```kotlin
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("SKU","12345");
        BlotoutAnalytics.INSTANCE.capture("custom event",eventInfo);
```

## capturePersonal
PII (Personal Identifiable Information) events are like developer codified events that carry sensitive information related to the user.
PHI ( Protected Health Information) events are like PII, but carry user’s private health information.
In Blotout managed or deployed Infrastructure, PII and PHI events data is encrypted using asymmetric encryption algorithms and provides access to authenticated users only.

#### Input
`-capturePersonal(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean)`

|||||
|---|---|---|---|
| `eventName` | `String` |  | Name of the event that you are sending |
| `eventInfo` | `Object` | | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |
| `isPHI` | `Boolean` | Optional | You can specify specific event type to an event|


#### Example
```kotlin
        HashMap<String,Object> PIIInfo = new HashMap<>();
        eventInfo.put("emailId","developers@blotout.io");
        BlotoutAnalytics.INSTANCE.capturePersonal("custom phi event",PIIInfo,true);

        HashMap<String,Object> PHIInfo = new HashMap<>();
        eventInfo.put("bloodType","A+");
        BlotoutAnalytics.INSTANCE.capturePersonal("custom phi event",PHIInfo,true);
```

## getUserId
The `getUserId` method allows you to go get Blotout user id that is linked to all data that is sent to the server.

#### Output
Returns user ID as `string`.

#### Example
```kotlin
String userId = BlotoutAnalytics.INSTANCE.getUserId()
```


## enable
The `enable` method allows you to enable/disable the sending of analytics data. Enabled by default.

#### Example
```kotlin
BlotoutAnalytics.INSTANCE.enable(true)
```
