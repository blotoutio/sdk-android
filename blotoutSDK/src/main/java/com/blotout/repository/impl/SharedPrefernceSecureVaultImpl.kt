package com.blotout.repository.impl

import android.content.SharedPreferences
import com.blotout.repository.data.SharedPrefernceSecureVault

class SharedPrefernceSecureVaultImpl(private val prefernce:SharedPreferences, private val cryptoService: String) : SharedPrefernceSecureVault{

    override fun storeString(key: String, value: String) {
        prefernce.edit().putString(key,value).apply()
    }

    override fun fetchString(key: String): String {
        val data = prefernce.getString(key,"")
        data!!.let { return it }
    }
}