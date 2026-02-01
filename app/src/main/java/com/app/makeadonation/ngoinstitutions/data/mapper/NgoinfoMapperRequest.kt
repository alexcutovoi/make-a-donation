package com.app.makeadonation.ngoinstitutions.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.ngoinstitutions.data.model.NgoInfoRequest
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import kotlin.String

class NgoinfoMapperRequest : BaseMapper<NgoInfo, NgoInfoRequest> {
    override fun generate(input: NgoInfo): NgoInfoRequest {
        return NgoInfoRequest(
            id = input.id,
            name = input.name,
            info = input.info,
            imageLink = input.imageLink,
            companyId = input.companyId
        )
    }
}