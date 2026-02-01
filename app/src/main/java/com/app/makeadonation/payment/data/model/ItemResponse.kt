package com.app.makeadonation.payment.data.model

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("description") val description: String,
    @SerializedName("details") val details: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("reference") val reference: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("unitOfMeasure") val unitOfMeasure: String,
    @SerializedName("unitPrice") val unitPrice: Int
)
