package com.app.makeadonation.ngolistdonations.data.repository

import android.net.Uri
import com.app.makeadonation.payment.PaymentCoordinator
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NGOListDonationsRepositoryImpl : NGOListDonationsRepository {
    override suspend fun listDonations(page: Int, numberOfItems: Int) = withContext(Dispatchers.IO) {
        runCatching {
            PaymentCoordinator.listPayments(page, numberOfItems)
        }.getOrThrow()
    }

    /*override suspend fun donate(donationValue: Long) = withContext(Dispatchers.IO) {
        runCatching {
            PaymentCoordinator.createOrderRequest(donationValue)
        }.getOrThrow()
    }*/

    override fun handlePaymentList(data: Uri): PaymentResult {
        runCatching {
            return PaymentCoordinator.getPaymentList(data)
        }.getOrThrow()
    }

    /*private fun selectNGOsToLoad(ngoCategoryId: Int): String {
        val ngos = hashMapOf(
            0 to "environment_ngos.json",
            1 to "education_ngos.json",
            2 to "social_ngos.json",
            3 to "research_ngos.json"
        )

        return ngos[ngoCategoryId]!!
    }*/
}