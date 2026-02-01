package com.app.makeadonation.common

import android.content.Context
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

object Utils {
    inline fun <reified T> retrieveObjectFromFile(file: String): T {
        val jsonString = MakeADonationApplication.getApplicationContext()
            .assets.open(file).bufferedReader().use { it.readText() }

        return Gson().fromJson(jsonString,  object : TypeToken<T>() {}.type)
    }

    inline fun <reified T> retrieveObject(data: String, clazz: Class<T>): T {
        return Gson().fromJson(data,  clazz)
    }

    fun hasField(fieldName: String, data: String): Boolean {
        return JSONObject(data).has(fieldName)
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
            )
        return sdf.format(Date())
    }

    fun formatCurrency(value: Int):String {
        val locale = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        locale.currency = Currency.getInstance("BRL")

        return locale.format(value / 100.0)
    }

    fun showDialog(context: Context, title: String, description: String) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}