package com.app.makeadonation.ngoinstitutions.data.repository

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult

interface NGOInstitutionsRepository {
    suspend fun retrieveNGOs(ngoCategoryId: Int) : List<NgoInfoResponse>
    suspend fun donate(donationValue: Long): Uri
    fun handlePayment(data: Uri): PaymentResult
}