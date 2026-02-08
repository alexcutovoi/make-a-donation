package com.app.makeadonation.payment.data.model

data class ItemRequest(
    val name: String,
    val quantity: Int = 1,
    val sku: String,
    val unitOfMeasure: String,
    val unitPrice: Long
)
