package com.app.makeadonation.payment.domain.entity

import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.data.model.SuccessResponse

sealed interface PaymentResult {
    data class Success(val response: SuccessResponse) : PaymentResult
    data class Error(val response: ErrorResponse) : PaymentResult
    data class Cancel(val response: ErrorResponse) : PaymentResult
}