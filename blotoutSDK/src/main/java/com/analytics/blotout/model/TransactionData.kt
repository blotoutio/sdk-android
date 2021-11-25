package com.analytics.blotout.model


data class TransactionData (
    var transaction_id : String,
    var transaction_currency : String?,
    var transaction_payment : String?,
    var transaction_total : Double?,
    var transaction_discount : Int?,
    var transaction_shipping : Int?,
    var transaction_tax : Double?
)