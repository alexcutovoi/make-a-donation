package com.app.makeadonation.ngoinstitutions.data.model

data class NgoDonationInfoRequest (
    val donatedValue: Long,
    val donationId: String,
    val donationDate: String,
    val authDonationCode: String,
    val operatorTransactionCode: String,
    val status: String,
    val ngoInfo: NgoInfoRequest
)