package com.app.makeadonation.common

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import org.koin.java.KoinJavaComponent.inject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

object Utils {
    private val textProvider: TextProvider by inject(TextProvider::class.java)

    inline fun <reified T> retrieveObjectFromFile(file: String): T {
        val jsonString = MakeADonationApplication.getApplicationContext()
            .assets.open(file).bufferedReader().use { it.readText() }

        return retrieveObject(jsonString)
    }

    inline fun <reified T> retrieveObject(data: String): T {
        return Gson().fromJson(data,  object : TypeToken<T>() {}.type)
    }

    fun  <T> transformToJJsom(obj: T): String {
        return Gson().toJson(obj).toString()
    }

    fun <T> encodeToBase64(order: T): String {
        val data = transformToJJsom(order).toByteArray(Charsets.UTF_8)

        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun decodeFromBase64(data: Uri, queryParameter: String): String? {
        return data.getQueryParameter(queryParameter)?.let {
            Base64.decode(it, Base64.DEFAULT).decodeToString()
        }
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

    fun formatToRegularDate(date: String): String {
        val inputFormat = SimpleDateFormat(
            "MMM d, yyyy h:mm:ss a",
            Locale.ENGLISH
        )

        val outputFormat = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        )

        val date = inputFormat.parse(date)
        return outputFormat.format(date!!)
    }

    fun formatCurrency(value: Long):String {
        val locale = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        locale.currency = Currency.getInstance("BRL")

        return locale.format(value / 100.0)
    }

    fun showDialog(
        context: Context,
        title: String,
        description: String,
        action: (() -> Unit)? = null) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton(
                textProvider.getText(R.string.ok)
            ) { dialog, _ ->
                action?.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(
                textProvider.getText(R.string.cancel)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}