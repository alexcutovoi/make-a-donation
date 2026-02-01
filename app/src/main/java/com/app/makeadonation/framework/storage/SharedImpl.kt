package com.app.makeadonation.framework.storage

import android.content.SharedPreferences

class SharedImpl(
    private val preferences: SharedPreferences
) : Shared {
    private val editor = preferences.edit()

    override fun writeString(key: String, value: String) {
        editor.putString(key, value).apply {
            apply()
        }
    }

    override fun getString(key: String): String? {
        return preferences.getString(key, EMPTY_STRING)
    }

    override fun clear() {
        editor.run {
            clear()
            apply()
        }
    }

    private companion object {
        const val EMPTY_STRING = ""
    }
}