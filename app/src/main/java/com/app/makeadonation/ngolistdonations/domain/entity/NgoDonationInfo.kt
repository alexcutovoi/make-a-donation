package com.app.makeadonation.ngolistdonations.domain.entity

data class NgoDonationInfo(
    val donatedValue: Long,
    val donationId: String,
    val donationDate: String,
    val authDonationCode: String,
    val operatorTransactionCode: String,
    val status: String,
    val ngoInfo: NgoInfo
) {
    private enum class Statuses(val description: String) {
        PAID("Pago"),
        ENTERED("Entrado"),
        CANCELED("Cancelado")
    }

    fun isCancelled(): Boolean {
        return status == Statuses.CANCELED.name
    }

    fun getStatusDescription(): String {
        return Statuses.entries.first { it.name == status }.description
    }
}