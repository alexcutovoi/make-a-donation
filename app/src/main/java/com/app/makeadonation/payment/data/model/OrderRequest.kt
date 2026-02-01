package com.app.makeadonation.payment.data.model

data class OrderRequest (
    val accessToken: String,
    val clientID: String,
    val reference: String? = null,
    val merchantCode: String? = null,
    val email: String? = null,
    val installments: Int = 1,
    val items: List<ItemRequest>,
    val paymentCode: String,
    val value: String
)