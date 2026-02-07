package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.domain.entity.Error

class ErrorResponseMapper : BaseMapper<ErrorResponse, Error>{
    override fun generate(input: ErrorResponse): Error {
        return Error(
            code = input.code,
            reason = input.reason
        )
    }
}