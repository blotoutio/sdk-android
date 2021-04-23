package com.blotout.repository.data

interface SharedPrefernceSecureVault {
    fun storeString(key:String,value:String)
    fun fetchString(key:String) : String
}