package com.app.makeadonation.ngoinstitutions.domain.usecase

import com.app.makeadonation.ngoinstitutions.data.mapper.NGOInstitutionsMapperResponse
import com.app.makeadonation.ngoinstitutions.data.repository.NGOInstitutionsRepository
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
}