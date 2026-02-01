package com.app.makeadonation.ngolistdonations.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.data.mapper.NGOInstitutionsMapperResponse
import com.app.makeadonation.ngoinstitutions.data.repository.NGOInstitutionsRepository
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

    /*override suspend fun donate(donationValue: Long) = flow {
        emit(
            ngoListDonationsRepository.donate(donationValue)
        )
    }*/

    override fun handlePaymentList(data: Uri): PaymentResult {
        return ngoListDonationsRepository.handlePaymentList(data)
    }
}