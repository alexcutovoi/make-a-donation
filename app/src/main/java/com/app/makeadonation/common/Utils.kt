package com.app.makeadonation.common

import com.app.makeadonation.MakeADonationApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    inline fun <reified T> retrieveJson(file: String): T {
        val jsonString = MakeADonationApplication.getApplicationContext()
            .assets.open(file).bufferedReader().use { it.readText() }

        return Gson().fromJson(jsonString,  object : TypeToken<T>() {}.type)
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
            )
        return sdf.format(Date())
    }
}