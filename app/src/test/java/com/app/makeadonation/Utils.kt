package com.app.makeadonation

import android.net.Uri
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object Utils {
    inline fun <reified T> retrieveObjectFromFile(file: String): T {
        val loader = javaClass.classLoader ?: ClassLoader.getSystemClassLoader()
        val jsonString = loader.getResourceAsStream(file).bufferedReader().use { it.readText() }

        return retrieveObject(jsonString)
    }

    inline fun <reified T> retrieveObject(data: String): T {
        return Gson().fromJson(data,  object : TypeToken<T>() {}.type)
    }

    fun  <T> transformToJson(obj: T): String {
        return Gson().toJson(obj).toString()
    }

    fun <T> encodeToBase64(order: T): String {
        val data = transformToJson(order).toByteArray(Charsets.UTF_8)

        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun decodeFromBase64(data: Uri, queryParameter: String): String? {
        return data.getQueryParameter(queryParameter)?.let {
            Base64.decode(it, Base64.DEFAULT).decodeToString()
        }
    }

    fun formatCurrency(value: Long):String {
        val locale = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        locale.currency = Currency.getInstance("BRL")

        return locale.format(value / 100.0)
    }
}