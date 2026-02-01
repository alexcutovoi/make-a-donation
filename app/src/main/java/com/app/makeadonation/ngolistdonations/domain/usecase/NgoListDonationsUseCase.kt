package com.app.makeadonation.ngolistdonations.domain.usecase

import android.net.Uri
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.flow.Flow

interface NgoListDonationsUseCase {
    suspend fun listDonations(page: Int, numberOfItems: Int) : Flow<Uri>
    fun handlePaymentList(data: Uri): PaymentResult
}