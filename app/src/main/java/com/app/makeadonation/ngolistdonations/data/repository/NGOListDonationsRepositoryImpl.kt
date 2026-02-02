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

    override fun handlePaymentList(data: Uri): PaymentResult {
        runCatching {
            return PaymentCoordinator.getPaymentList(data)
        }.getOrThrow()
    }

    override suspend fun cancelDonation(id: String, cieloCode: String, authCode: String, value: Long) = withContext(Dispatchers.IO) {
        runCatching {
            PaymentCoordinator.cancelOrder(id, cieloCode, authCode, value)
        }.getOrThrow()
    }
}