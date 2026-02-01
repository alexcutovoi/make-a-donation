package com.app.makeadonation.ngolistdonations.data.repository

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult

interface NGOListDonationsRepository {
    suspend fun listDonations(page: Int, numberOfItems: Int): Uri
    //suspend fun donate(donationValue: Long): Uri
    fun handlePaymentList(data: Uri): PaymentResult
}