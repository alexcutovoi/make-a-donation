package com.app.makeadonation.ngolistdonations.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.flow.Flow

interface NgoListDonationsUseCase {
    suspend fun listDonations(page: Int, numberOfItems: Int) : Flow<Uri>
    //uspend fun donate(donationValue: Long) : Flow<Uri>
    fun handlePaymentList(data: Uri): PaymentResult
}