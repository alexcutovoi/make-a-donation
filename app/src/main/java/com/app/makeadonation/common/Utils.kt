package com.app.makeadonation.common

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.app.makeadonation.MakeADonationApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.text.SimpleDateFormat
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

    fun showDialog(context: Context, title: String, description: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}