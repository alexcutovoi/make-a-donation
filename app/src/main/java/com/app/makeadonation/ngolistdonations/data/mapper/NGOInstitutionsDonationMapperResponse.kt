package com.app.makeadonation.ngolistdonations.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.ngolistdonations.data.model.NgoDonationInfoResponse
import com.app.makeadonation.ngolistdonations.domain.entity.NgoDonationInfo

class NGOInstitutionsDonationMapperResponse : BaseMapper<NgoDonationInfoResponse, NgoDonationInfo> {
    override fun generate(input: NgoDonationInfoResponse) = NgoDonationInfo(
        donationId = input.donationId,
        donatedValue = input.donatedValue,
        donationDate = input.donationDate,
        authDonationCode = input.authDonationCode,
        operatorTransactionCode = input.operatorTransactionCode,
        status = input.status,
        ngoInfo = NGOInstitutionsMapperResponse().generate(input.ngoInfo)
    )
}