package com.app.makeadonation.ngoinstitutions.data.repository

import android.net.Uri
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse

interface NGOInstitutionsRepository {
    suspend fun retrieveNGOs(ngoCategoryId: Int) : List<NgoInfoResponse>
    suspend fun donate(donationValue: Long): Uri
}