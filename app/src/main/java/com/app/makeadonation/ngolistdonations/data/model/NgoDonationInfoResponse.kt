package com.app.makeadonation.ngolistdonations.data.model

import com.google.gson.annotations.SerializedName

data class NgoDonationInfoResponse(
    @SerializedName("donatedValue") val donatedValue: Long,
    @SerializedName("donationId") val donationId: String,
    @SerializedName("donationDate") val donationDate: String,
    @SerializedName("authDonationCode") val authDonationCode: String,
    @SerializedName("operatorTransactionCode") val operatorTransactionCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("ngoInfo") val ngoInfo: NgoInfoResponse
)
