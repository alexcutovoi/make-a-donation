package com.app.makeadonation.ngoinstitutions.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.flow.Flow

interface NgoInstitutionsUseCase {
    suspend fun retrieveNGOs(ngoCategoryId: Int) : Flow<List<NgoInfo>>
    suspend fun donate(donationValue: Long) : Flow<Uri>
    suspend fun storeDonation(
        donationValue: Long,
        donationId: String,
        donationDate: String,
        authDonationCode: String,
        operatorTransactionCode: String,
        status: String,
        ngoInfo: NgoInfo
    ) : Flow<Unit>
    fun handlePayment(data: Uri): PaymentResult
}