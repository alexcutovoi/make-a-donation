package com.app.makeadonation.payment.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("reason") val reason: String,
)
