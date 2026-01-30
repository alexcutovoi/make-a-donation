package com.app.makeadonation.ngoinstitutions.data.repository

import com.app.makeadonation.common.Utils
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse
import com.app.makeadonation.payment.PaymentCoordinator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NGOInstitutionsRepositoryImpl : NGOInstitutionsRepository{
    override suspend fun retrieveNGOs(ngoCategoryId: Int) : List<NgoInfoResponse>  = withContext(Dispatchers.IO) {
        runCatching {
            Utils.retrieveJson<List<NgoInfoResponse>>(
                selectNGOsToLoad(ngoCategoryId)
            )
        }.getOrThrow()
    }

    override suspend fun donate(donationValue: Long) = withContext(Dispatchers.IO) {
        runCatching {
            PaymentCoordinator.createOrderRequest(donationValue)
        }.getOrThrow()
    }

    private fun selectNGOsToLoad(ngoCategoryId: Int): String {
        val ngos = hashMapOf(
            0 to "environment_ngos.json",
            1 to "eduction_ngos.json",
            2 to "social_ngos.json",
            3 to "research_ngos.json"
        )

        return ngos[ngoCategoryId]!!
    }
}