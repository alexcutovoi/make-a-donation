package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.Error
import com.app.makeadonation.payment.data.model.ErrorResponse

class ErrorResponseMapper : BaseMapper<ErrorResponse, Error>{
    override fun generate(input: ErrorResponse): Error {
        return Error(
            code = input.code,
            reason = input.reason
        )
    }
}