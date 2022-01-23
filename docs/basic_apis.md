# Basic API's

## init
The `init` method is used for initializing SDK. This sets all required configurations and also sends system event `sdk_start` which allows it to record user.

#### Input
`BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration);`

|||||
|---|---|---|---|
| `applicationContext` | `Object` | Application Context |
| `blotoutAnalyticsConfiguration` | `BlotoutAnalyticsConfiguration` | This Model contains information related to SDK initialization |
| `comletionHandler` | `CompletionHandler`| Return callback for sdk success and failure|

## BlotoutAnalyticsConfiguration

|||||
|---|---|---|---|
| `setBlotoutSDKKey` | `String` | Required | Application token that you can get in your dashboard. |
| `setEndPointUrl` | `String` | Required | Url where you will be sending data. |


#### Example
```java
BlotoutAnalyticsConfiguration blotoutAnalyticsConfiguration = new BlotoutAnalyticsConfiguration();
blotoutAnalyticsConfiguration.setBlotoutSDKKey("EADAH5FV8B5MMVZ");
blotoutAnalyticsConfiguration.setEndPointUrl("https://stage.blotout.io/sdk/");
CompletionHandler comletionHandler = new CompletionHandler{
    @Override
    fun onSuccess() {

    }

    @Override
    fun onError() {

    }
}

BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration,comletionHandler);
```

## capture
The `capture` method is used to record developer events. This allows you to send custom events to the server when a user is interacting with the app. For example, one custom event would be when a user adds an item to a cart.

#### Input
`capture(eventName: String, eventInfo: HashMap<String, Any>)`

|||||
|---|---|---|---|
| `eventName` | `String` | Required | Name of the event that you are sending |
| `eventInfo` | `Object` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### Example
```Java
HashMap<String,Object> eventInfo = new HashMap<>();
eventInfo.put("SKU","12345");
BlotoutAnalytics.INSTANCE.capture("custom event", eventInfo);
```

## capturePersonal
PII (Personal Identifiable Information) events are like developer codified events that carry sensitive information related to the user.
PHI ( Protected Health Information) events are like PII, but carry user’s private health information.
In Blotout managed or deployed Infrastructure, PII and PHI events data is encrypted using asymmetric encryption algorithms and provides access to authenticated users only.

#### Input
`capturePersonal(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean)`

|||||
|---|---|---|---|
| `eventName` | `String` | Required | Name of the event that you are sending |
| `eventInfo` | `Object` | Required | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |
| `isPHI` | `Boolean` | Optional | You can specify specific event type to an event|

#### Example
```Java
HashMap<String,Object> PIIInfo = new HashMap<>();
eventInfo.put("emailId","developers@blotout.io");
BlotoutAnalytics.INSTANCE.capturePersonal("pii event", PIIInfo, true);
```

```Java
HashMap<String,Object> PHIInfo = new HashMap<>();
eventInfo.put("bloodType","A+");
BlotoutAnalytics.INSTANCE.capturePersonal("phi event", PHIInfo, true);
```

## getUserId
The `getUserId` method allows you to go get Blotout user id that is linked to all data that is sent to the server.

#### Output
Returns user ID as `string`.

#### Example
```kotlin
var userId = BlotoutAnalytics.INSTANCE.getUserId()
```

## enable
The `enable` method allows you to enable/disable the sending of analytics data. Enabled by default.

|||||
|---|---|---|---|
| `enable` | `Boolean` | Required | Enable/disable SDK. |

#### Example
```kotlin
BlotoutAnalytics.INSTANCE.enable(true)
```

## transaction

The `transaction` method allows you to record tranasctions in your system, like purchase in ecommerce.

#### Input

|                  |          |          |                                                                                                                                 |
| ---------------- | -------- | -------- | ------------------------------------------------------------------------------------------------------------------------------- |
| `transactionData`      | `TransactionData` | Required | See data table.                                                                                                                 |
| `additionalData` | `HashMap` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |
                                                                                         |

#### Data

|              |          |          |                                                            |
| ------------ | -------- | -------- | ---------------------------------------------------------- |
| `transaction_id` | `String` | Required | Transaction ID.           |
| `transaction_currency`   | `String` | Optional | Currency used for the transaction. Example: `EUR` |
| `transaction_payment`   | `String` | Optional | Payment type used in the transaction. Example: `credit-card` |
| `transaction_total`   | `Double` | Optional | Total amount for the transaction. Example `10.50` |
| `transaction_discount`   | `Double` | Optional | Discount that was applied in the transaction. Example: `2.1` |
| `transaction_shipping`   | `Double` | Optional | Shipping that was charged in the transaction. Example: `5.0` |
| `transaction_tax`   | `Double` | Optional | How much tax was applied in the transaction. Example: `1.21` |

#### Example

{% tabs basic %}
{% tab basic kotlin %}

```kotlin
val transactionData = TransactionData(transaction_id = "123423423",transaction_currency = "$",transaction_payment = "1234",transaction_total = 1234.00,
                                    transaction_discount = 12,transaction_shipping = null,transaction_tax = 10.25)
        val withInformation = hashMapOf<String, Any>()
        withInformation.put("Language","EN")
        BlotoutAnalytics.transaction(transactionData, withInformation)

```

{% endtab %}
{% tab basic Java %}

```Java
TransactionData transactionData = new TransactionData("123423423",
                                    "$","1234",1234.00,
                                12,null,10.25);
BlotoutAnalytics.INSTANCE.transaction(transactionData,null);
```

{% endtab %}
{% endtabs %}

## item

The `item` method allows you to record item in your system, like add to cart in ecommerce.

#### Input

|                  |          |          |                                                                                                                                 |
| ---------------- | -------- | -------- | ------------------------------------------------------------------------------------------------------------------------------- |
| `data`      | `Item` | Required | See data table.                                                                                                                 |
| `additionalData` | `HashMap` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### Data

|              |          |          |                                                            |
| ------------ | -------- | -------- | ---------------------------------------------------------- |
| `item_id` | `String` | Required | Item ID.           |
| `item_name`   | `String` | Optional | Example: `Phone 4` |
| `item_sku`   | `String` | Optional | Example: `SHOP-01` |
| `item_category`   | `Array` | Optional | Example `['mobile', 'free-time]` |
| `item_currency`   | `String` | Optional | Currency of item price. Example: `EUR` |
| `item_price`   | `Double` | Optional | Example: `2.1` |
| `quantity`   | `Double` | Optional | Example: `3` |

#### Example

{% tabs basic %}
{% tab basic browser %}

```kotlin
val itemData = Item(item_id= "123423423", item_currency= "EUR", item_price= 10.5, quantity= 2)
BlotoutAnalytics.item(itemData,null);

```

{% endtab %}
{% tab basic node %}

```Java

Item itemData = new Item( "123423423",null,1,null, "EUR",  10.5,  2);
BlotoutAnalytics.INSTANCE.item(itemData,null);
```

{% endtab %}
{% endtabs %}

## persona

The `persona` method allows you to record persona in your system, like when user signs up or saves user profile.

#### Input

|                  |          |          |                                                                                                                                 |
| ---------------- | -------- | -------- | ------------------------------------------------------------------------------------------------------------------------------- |
| `data`      | `Object` | Required | See data table.                                                                                                                 |
| `additionalData` | `HashMap` | Optional | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |

#### Data

|              |          |          |                                                            |
| ------------ | -------- | -------- | ---------------------------------------------------------- |
| `persona_id` | `String` | Required | Persona ID.           |
| `persona_firstname`   | `String` | Optional | Example: `John` |
| `persona_lastname`   | `String` | Optional | Example: `Smith` |
| `persona_middlename`   | `String` | Optional | Example `Jack` |
| `persona_username`   | `String` | Optional | Example: `jsmith` |
| `persona_dob`   | `String` | Optional | Date of birth. Example: `04/30/2000` |
| `persona_email`   | `String` | Optional | Example: `john@domain.com` |
| `persona_number`   | `String` | Optional | Example: `+386 31 777 444` |
| `persona_address`   | `String` | Optional | Example: `Street 1` |
| `persona_city`   | `String` | Optional | Example: `San Francisco` |
| `persona_state`   | `String` | Optional | Example: `CA` |
| `persona_zip`   | `Double` | Optional | Example: `10000` |
| `persona_country`   | `String` | Optional | Example: `US` |
| `persona_gender`   | `String` | Optional | Example: `Female` |
| `persona_age`   | `Double` | Optional | Example: `22` |

#### Example

{% tabs basic %}
{% tab basic browser %}

```kotlin
val persona_item = Persona ( persona_id= "3434343", persona_gender= "female", persona_age= 22 )
BlotoutAnalytics.persona(persona_item,null);
```

{% endtab %}
{% tab basic node %}

```java

Persona persona_item = new Persona({ "3434343",null,null,null,null,null,null,null,null,null,null,null,null, gender: 'female', age: 22 })
BlotoutAnalytics.INSTANCE.persona(persona_item,null);
```

{% endtab %}
{% endtabs %}

