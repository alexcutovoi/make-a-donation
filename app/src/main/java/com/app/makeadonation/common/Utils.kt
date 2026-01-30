package com.app.makeadonation.common

import com.app.makeadonation.MakeADonationApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Utils {
    inline fun <reified T> retrieveJson(file: String): T {
        val jsonString = MakeADonationApplication.getApplicationContext()
            .assets.open(file).bufferedReader().use { it.readText() }

        return Gson().fromJson(jsonString,  object : TypeToken<T>() {}.type)
    }
}