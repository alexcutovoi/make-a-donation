package com.app.makeadonation.payment.domain.entity

data class Success(
    val createdAt: String,
    val id: String,
    val items: List<Item>,
    val notes: String,
    val number: String,
    val paidAmount: Int,
    val payments: List<Payment>,
    val pendingAmount: Int,
    val price: Int,
    val reference: String,
    val status: String,
    val type: String,
    val updatedAt: String
)
