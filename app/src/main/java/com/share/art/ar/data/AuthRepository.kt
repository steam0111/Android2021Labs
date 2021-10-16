package com.share.art.ar.data

import android.content.SharedPreferences
import java.util.*

class AuthRepository(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val USER_UUID_KEY = "USER_UUID_KEY"
    }

    fun getUuid(): String {
        var uuid = sharedPreferences.getString(USER_UUID_KEY, "")

        if (uuid.isNullOrEmpty()) {
            uuid = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(USER_UUID_KEY, uuid).apply()
        }

        return uuid
    }
}