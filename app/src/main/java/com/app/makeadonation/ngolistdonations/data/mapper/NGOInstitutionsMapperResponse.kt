package com.app.makeadonation.ngolistdonations.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoResponse
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo

class NGOInstitutionsMapperResponse : BaseMapper<NgoInfoResponse, NgoInfo> {
    override fun generate(input: NgoInfoResponse) = NgoInfo(
        id = input.id,
        name = input.name,
        imageLink = input.imageLink,
        info = input.info,
        companyId = input.companyId
    )
}