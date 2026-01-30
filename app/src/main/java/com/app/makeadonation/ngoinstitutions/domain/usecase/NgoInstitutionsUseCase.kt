package com.app.makeadonation.ngoinstitutions.domain.usecase

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import kotlinx.coroutines.flow.Flow

interface NgoInstitutionsUseCase {
    suspend fun retrieveNGOs(ngoCategoryId: Int) : Flow<List<NgoInfo>>
    suspend fun donate(donationValue: Long) : Flow<Uri>
}