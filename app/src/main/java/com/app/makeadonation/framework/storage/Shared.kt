package com.app.makeadonation.framework.storage

interface Shared {
    fun writeString(key: String, value: String)
    fun getString(key: String): String?
    fun clear()
}