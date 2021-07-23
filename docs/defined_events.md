# Defined Events

## mapID
The `mapID` method allows you to map external services to Blotout ID.

#### Input
`mapID(mapIDData: MapIDData, withInformation: HashMap<String, Any>?)`

|||||
|---|---|---|---|
| `mapIDData` | `MapIDData` | Required | See data table. |
| `eventInfo` | `Object` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### MapIDData
|              |          |          |                                                            |
| ------------ | -------- | -------- | ---------------------------------------------------------- |
| `externalID` | `String` | Required | External ID that you want to link to Blotout ID.           |
| `provider`   | `String` | Required | Provider that generated external ID, for example `hubspot` |


#### Example
```kotlin
HashMap<String,Any> eventInfo = new HashMap<>();
eventInfo.put("language","en");

var data = MapIDData()
data.externalID = "92j2jr230r-232j9j2342j3-jiji"
data.provider = "sass"

BlotoutAnalytics.INSTANCE.mapID(data, null);
BlotoutAnalytics.INSTANCE.mapID(data, eventInfo);
```
