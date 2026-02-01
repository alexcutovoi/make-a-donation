package com.app.makeadonation.ngoinstitutions.data.model

data class DonationInfoRequest (
    val donatedValue: Long,
    val donationId: String,
    val ngoInfo: NgoInfoRequest
)