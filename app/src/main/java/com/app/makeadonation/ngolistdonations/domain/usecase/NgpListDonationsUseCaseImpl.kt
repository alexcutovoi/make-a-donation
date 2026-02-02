package com.app.makeadonation.ngolistdonations.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngolistdonations.data.repository.NGOListDonationsRepository
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.flow.flow

class NgpListDonationsUseCaseImpl(
    private val ngoListDonationsRepository: NGOListDonationsRepository
) : NgoListDonationsUseCase {
    override suspend fun listDonations(page: Int, numberOfItems: Int) = flow {
        emit(
            ngoListDonationsRepository.listDonations(page, numberOfItems)
        )
    }

    override fun handlePaymentList(data: Uri): PaymentResult {
        return ngoListDonationsRepository.handlePaymentList(data)
    }

    override suspend fun cancelDonation(id: String, cieloCode: String, authCode: String, value: Long) = flow {
        emit(
        ngoListDonationsRepository.cancelDonation(id, cieloCode, authCode, value)
        )
    }
}