package com.app.makeadonation.ngolistdonations.data.repository

import android.net.Uri
import com.app.makeadonation.ngolistdonations.data.model.NgoDonationInfoResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult

interface NGOListDonationsRepository {
    suspend fun listDonations(page: Int, numberOfItems: Int): Uri
    suspend fun listDonationsFromServer(): List<NgoDonationInfoResponse>
    fun handlePaymentList(data: Uri): PaymentResult
    suspend fun cancelDonation(id: String, cieloCode: String, authCode: String, value: Long): Uri
}