package com.app.makeadonation.payment.domain.entity

data class Payment(
    val accessKey: String,
    val amount: Int,
    val applicationName: String,
    val authCode: String,
    val brand: String,
    val cieloCode: String,
    val description: String,
    val discountedAmount: Int,
    val externalId: String,
    val id: String,
    val installments: Int,
    val mask: String,
    val merchantCode: String,
    val paymentFields: PaymentFields,
    val primaryCode: String,
    val requestDate: String,
    val secondaryCode: String,
    val terminal: String
)
