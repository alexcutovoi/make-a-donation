package com.app.makeadonation.payment.data.model

data class ListOrdersRequest(
    val page: Int,
    val pageSize: Int,
    val accessToken: String,
    val clientID: String,
)
