package com.app.makeadonation.ngoinstitutions.domain.usecase

import android.net.Uri
import com.app.makeadonation.common.Utils
import com.app.makeadonation.ngoinstitutions.data.mapper.NGOInstitutionsMapperResponse
import com.app.makeadonation.ngoinstitutions.data.mapper.NgoinfoMapperRequest
import com.app.makeadonation.ngoinstitutions.data.model.NgoDonationInfoRequest
import com.app.makeadonation.ngoinstitutions.data.repository.NGOInstitutionsRepository
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.flow.flow

class NgpInstitutionsUseCaseImpl(
    private val ngoInstitutionsRepository: NGOInstitutionsRepository
) : NgoInstitutionsUseCase {
    override suspend fun retrieveNGOs(ngoCategoryId: Int) = flow {
        emit(
            ngoInstitutionsRepository.retrieveNGOs(ngoCategoryId).map {
                NGOInstitutionsMapperResponse().generate(it)
            }
        )
    }

    override suspend fun donate(donationValue: Long) = flow {
        emit(
            ngoInstitutionsRepository.donate(donationValue)
        )
    }

    override suspend fun storeDonation(
        donationValue: Long,
        donationId: String,
        donationDate: String,
        authDonationCode: String,
        operatorTransactionCode: String,
        status: String,
        ngoInfo: NgoInfo
    ) = flow {
        emit(
            ngoInstitutionsRepository.storeDonation(
                NgoDonationInfoRequest(
                    donationValue,
                    donationId,
                    Utils.formatToRegularDate(donationDate),
                    authDonationCode,
                    operatorTransactionCode,
                    status,
                    NgoinfoMapperRequest().generate(ngoInfo)
                )
            )
        )
    }

    override fun handlePayment(data: Uri): PaymentResult {
        return ngoInstitutionsRepository.handlePayment(data)
    }
}