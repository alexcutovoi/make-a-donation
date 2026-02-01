package com.app.makeadonation.payment.data.model

import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("accessKey") val accessKey: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("applicationName") val applicationName: String,
    @SerializedName("authCode") val authCode: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("cieloCode") val cieloCode: String,
    @SerializedName("description") val description: String,
    @SerializedName("discountedAmount") val discountedAmount: Int,
    @SerializedName("externalId") val externalId: String,
    @SerializedName("id") val id: String,
    @SerializedName("installments") val installments: Int,
    @SerializedName("mask") val mask: String,
    @SerializedName("merchantCode") val merchantCode: String,
    @SerializedName("paymentFields") val paymentFieldsResponse: PaymentFieldsResponse,
    @SerializedName("primaryCode") val primaryCode: String,
    @SerializedName("requestDate") val requestDate: String,
    @SerializedName("secondaryCode") val secondaryCode: String,
    @SerializedName("terminal") val terminal: String
)
