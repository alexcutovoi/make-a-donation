package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.SuccessResponse
import com.app.makeadonation.payment.domain.entity.Success

class SuccessResponseMapper: BaseMapper<SuccessResponse, Success> {
    override fun generate(input: SuccessResponse): Success {
        return Success(
            createdAt = input.createdAt,
            id = input.id,
            items = input.itemsResponse.map { ItemResponseMapper().generate(it) },
            notes = input.notes,
            number = input.number,
            paidAmount = input.paidAmount,
            payments = input.paymentsResponse.map { PaymentResponseMapper().generate(it) },
            pendingAmount = input.pendingAmount,
            price = input.price,
            reference = input.reference,
            status = input.status,
            type = input.type,
            updatedAt = input.updatedAt
        )
    }
}