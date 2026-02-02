package com.app.makeadonation.payment.data.model

data class DonationCancellationRequest (
    val id: String,
    val clientID: String,
    val accessToken: String,
    val cieloCode: String,
    val authCode: String,
    val value: Long
)