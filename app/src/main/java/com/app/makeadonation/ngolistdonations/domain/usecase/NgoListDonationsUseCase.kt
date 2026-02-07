package com.app.makeadonation.ngolistdonations.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngolistdonations.data.model.NgoDonationInfoResponse
import com.app.makeadonation.ngolistdonations.domain.entity.NgoDonationInfo
import com.app.makeadonation.payment.domain.entity.PaymentResult
import com.app.makeadonation.payment.domain.entity.Success
import kotlinx.coroutines.flow.Flow

interface NgoListDonationsUseCase {
    suspend fun listRegisteredDonations(page: Int, numberOfItems: Int) : Flow<Uri>
    suspend fun listRegisteredDonationsFromServer(registeredDonations: List<Success>) : Flow<List<NgoDonationInfo>>
    fun handlePaymentList(data: Uri): PaymentResult
    suspend fun cancelDonation(id: String, cieloCode: String, authCode: String, value: Long) : Flow<Uri>
}