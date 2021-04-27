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

    override fun storeLong(key: String, value: Long) {
        prefernce.edit().putLong(key,value).apply()
    }

    override fun fetchLong(key: String): Long {
        val data = prefernce.getLong(key,0)
        data!!.let { return it }
    }
}