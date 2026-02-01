package com.app.makeadonation.ngolistdonations.data.repository

import android.net.Uri
import com.app.makeadonation.payment.domain.entity.PaymentResult

interface NGOListDonationsRepository {
    suspend fun listDonations(page: Int, numberOfItems: Int): Uri
    fun handlePaymentList(data: Uri): PaymentResult
}