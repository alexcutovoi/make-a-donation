package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.PaymentResponse
import com.app.makeadonation.payment.domain.entity.Payment

class PaymentResponseMapper : BaseMapper<PaymentResponse, Payment> {

    override fun generate(input: PaymentResponse): Payment {
        return Payment(
            accessKey = input.accessKey,
            amount = input.amount,
            applicationName = input.applicationName,
            authCode = input.authCode,
            brand = input.brand,
            cieloCode = input.cieloCode,
            description = input.description,
            discountedAmount = input.discountedAmount,
            externalId = input.externalId,
            id = input.id,
            installments = input.installments,
            mask = input.mask,
            merchantCode = input.merchantCode,
            paymentFields = PaymentFieldsResponseMapper().generate(input.paymentFieldsResponse),
            primaryCode = input.primaryCode,
            requestDate = input.requestDate,
            secondaryCode = input.secondaryCode,
            terminal = input.terminal
        )
    }
}