package com.app.makeadonation.payment.data.model

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("id") val id: String,
    @SerializedName("items") val itemsResponse: List<ItemResponse>,
    @SerializedName("notes") val notes: String,
    @SerializedName("number") val number: String,
    @SerializedName("paidAmount") val paidAmount: Int,
    @SerializedName("payments") val paymentsResponse: List<PaymentResponse>,
    @SerializedName("pendingAmount") val pendingAmount: Int,
    @SerializedName("price") val price: Long,
    @SerializedName("reference") val reference: String,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("updatedAt") val updatedAt: String
)
