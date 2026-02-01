package com.app.makeadonation.payment.data.model

import com.google.gson.annotations.SerializedName

data class ListOrdersResponse (
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("results") val orders: List<SuccessResponse>,
    @SerializedName("totalItems") val totalOrdersNum: Int,
    @SerializedName("totalPages") val totalPagesNum: Int
)
