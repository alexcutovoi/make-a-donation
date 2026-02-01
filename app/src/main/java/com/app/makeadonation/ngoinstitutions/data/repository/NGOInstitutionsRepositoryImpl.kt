package com.app.makeadonation.ngoinstitutions.data.repository

import android.net.Uri
import com.app.makeadonation.common.Utils
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse
import com.app.makeadonation.payment.PaymentCoordinator
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NGOInstitutionsRepositoryImpl : NGOInstitutionsRepository {
    override suspend fun retrieveNGOs(ngoCategoryId: Int) : List<NgoInfoResponse>  = withContext(Dispatchers.IO) {
        runCatching {
            Utils.retrieveObjectFromFile<List<NgoInfoResponse>>(
                selectNGOsToLoad(ngoCategoryId)
            )
        }.getOrThrow()
    }

    override suspend fun donate(donationValue: Long) = withContext(Dispatchers.IO) {
        runCatching {
            PaymentCoordinator.createOrderRequest(donationValue)
        }.getOrThrow()
    }

    override fun handlePayment(data: Uri): PaymentResult {
        runCatching {
            return PaymentCoordinator.getPaymentResponse(data)
        }.getOrThrow()
    }

    private fun selectNGOsToLoad(ngoCategoryId: Int): String {
        val ngos = hashMapOf(
            0 to "environment_ngos.json",
            1 to "education_ngos.json",
            2 to "social_ngos.json",
            3 to "research_ngos.json"
        )

        return ngos[ngoCategoryId]!!
    }
}