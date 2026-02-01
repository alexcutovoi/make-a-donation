package com.app.makeadonation.payment.domain.entity

data class Item(
    val description: String,
    val details: String,
    val id: String,
    val name: String,
    val quantity: Int,
    val reference: String,
    val sku: String,
    val unitOfMeasure: String,
    val unitPrice: Int
)
