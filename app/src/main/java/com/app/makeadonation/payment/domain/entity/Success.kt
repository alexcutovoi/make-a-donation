package com.app.makeadonation.payment.domain.entity

import android.util.Log

data class Success(
    val createdAt: String,
    val id: String,
    val items: List<Item>,
    val notes: String,
    val number: String,
    val paidAmount: Int,
    val payments: List<Payment>,
    val pendingAmount: Int,
    val price: Long,
    val reference: String,
    val status: String,
    val type: String,
    val updatedAt: String
) {
    private enum class Statuses(name: String) {
        PAID("Pago"),
        ENTERED("Entrado"),
        CANCELED("Cancelado")
    }

    fun isCancelled(): Boolean {
        return status == Statuses.CANCELED.toString()
    }

    fun getStatusDescription(): String {
        Log.e("ERROR","STATUS: $status")
        return Statuses.entries.first { it.name == status }.name
    }
}
