# API

## init
The `initializeAnalyticsEngine` method is used for initializing SDK. This sets all required configurations and also sends system event `sdk_start` which allows it to record user.

#### Input
`BlotoutAnalytics.getInstance().initializeAnalyticsEngine(applicationContext,"BlotoutSDKKey","EndPointUrl");`

|||||
|---|---|---|---|
| `applicationContext` | `Object` | Application token that you can get in your dashboard |
| `BlotoutSDKKey` | `String` | Application token that you can get in your dashboard |
| `EndPointUrl` | `String` | Url where you will be sending data |

#### Example
```js
BlotoutAnalytics.getInstance().
    initializeAnalyticsEngine(applicationContext,
    "BlotoutSDKKey",
    "EndPointUrl");
```

## capture events
The `logEvent` method is used to record developer events. This allows you to send custom events to the server when a user is interacting with the app. For example, one custom event would be when a user adds an item to a cart.

## Non-Timed Events
Non-Timed events are generally events which are not time bound and do not contain duration information. For example, the Home Page loaded is non-timed but Home page loading started and home page loading ended, when grouped together, can be a timed event.
These events are categorized under two main categories in Blotout’s SDK 
1: SystemEvents:
System events are those which the Blotout SDK captures automatically like App Launch, App Terminated etc. These kinds of non-timed events do not require any developer intervention except enable or disable.

2: Developer Events:
Developer Events are those which developers codify in the Application code with the help of Blotout’s SDK and SDK sync with Blotout’s server, like “iPhone added to cart“.

```html
public void logEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo);
public void logEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, Date eventTime);
```

## Timed Events
Timed events are generally events which are time bound and contain duration information as explained above. Timed events w.r.t Blotout’s SDK are developer codified events only.
When developers want to log an event along with duration, then they can use the below mentioned APIs to log a timed event.

There are two methods mentioned above:
● startTimedEvent : This method will start the timer for the event name mentioned in the method call.
● endTimedEvent : This method will end the timer for the event name mentioned in the method call.

```html
public void startTimedEvent(@NonNull String eventName, @Nullable HashMap<String, Object> startEventInfo);
public void endTimedEvent(@NonNull String eventName, @Nullable HashMap<String, Object> endEventInfo);
```

## PII & PHI Events
PII (Personal Identifiable Information) events are like developer codified events that carry sensitive information related to User.
PHI ( Protected Health information) events are like PII but carries user’s private health information
In Blotout managed or deployed Infrastructure, PII and PHI events data is encrypted using asymmetric encryption algorithms and provides access to authenticated users only.
Below methods can be used to log PII and PHI information.

```html
public void logPIIEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo,Date eventTime);
public void logPHIEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo,Date eventTime);
```

|||||
|---|---|---|---|
| `eventName` | `String` |  | Name of the event that you are sending |
| `eventInfo` | `Object` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |
| `eventTime` | `Date` | Optional | You can give specific time to an event|


#### Example
```js
HashMap<String,Object> eventInfo = new HashMap<>();
eventInfo.put("emailId","developers@blotout.io");
eventInfo.put("gender","Male");

BlotoutAnalytics.getInstance().logEvent("LoginView",null);
BlotoutAnalytics.getInstance().logPHIEvent("PHIEvent",eventInfo,null);
BlotoutAnalytics.getInstance().logPIIEvent("PIIEvent",eventInfo,null);

```


## mapID
The `mapID` method allows you to map external services to Blotout ID.

#### Input
`public void mapId(@NonNull String id, @NonNull String provider, @Nullable HashMap<String, Object> eventInfo)`

|||||
|---|---|---|---|
| `externalID` | `String` |  | External ID that you want to link to Blotout ID |
| `provider` | `String` |  | Provider that generated external ID, for example `hubspot` |
| `eventInfo` | `Object` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### Example
```js
HashMap<String,Object> eventInfo = new HashMap<>();
eventInfo.put("emailId","support@blotout.io");
eventInfo.put("gender","Male");

BlotoutAnalytics.getInstance().mapId("92j2jr230r-232j9j2342j3-jiji","hubspot",null);
BlotoutAnalytics.getInstance().mapId("92j2jr230r-232j9j2342j3-jiji","hubspot",eventInfo);
```



