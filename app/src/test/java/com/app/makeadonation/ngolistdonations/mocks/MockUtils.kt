package com.app.makeadonation.ngolistdonations.mocks

import com.app.makeadonation.ngolistdonations.domain.entity.NgoDonationInfo
import com.app.makeadonation.ngolistdonations.domain.entity.NgoInfo

object MockUtils {
    fun getNgoInfo() = NgoInfo(
        "0",
        "Enviromental NGO",
        "Enviromental NGO NGO fights for environmental causes",
        "forest",
        ""
    )

    fun getDonationInfo() = NgoDonationInfo(
        donatedValue = 1000L,
        donationId = "donation-1",
        donationDate = "01/01/2026",
        authDonationCode = "auth123",
        operatorTransactionCode = "cielo456",
        status = "PAID",
        ngoInfo = getNgoInfo()
    )
}
