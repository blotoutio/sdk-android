package com.analytics.blotout.model

class Item(val item_id : String,
           val item_name : String?,
           val item_sku : String?,
           val item_category : List<String>?,
           val item_price : Int?,
           val item_currency : String?,
           val item_quantity : Int?)
