package com.app.makeadonation.payment.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.payment.data.model.PaymentFieldsResponse
import com.app.makeadonation.payment.domain.entity.PaymentFields

class PaymentFieldsResponseMapper : BaseMapper<PaymentFieldsResponse, PaymentFields> {

    override fun generate(input: PaymentFieldsResponse): PaymentFields {
        return PaymentFields(
            isDoubleFontPrintAllowed = input.isDoubleFontPrintAllowed,
            hasPassword = input.hasPassword,
            primaryProductCode = input.primaryProductCode,
            isExternalCall = input.isExternalCall,
            primaryProductName = input.primaryProductName,
            receiptPrintPermission = input.receiptPrintPermission,
            isOnlyIntegrationCancelable = input.isOnlyIntegrationCancelable,
            upFrontAmount = input.upFrontAmount,
            creditAdminTax = input.creditAdminTax,
            firstQuotaDate = input.firstQuotaDate,
            isFinancialProduct = input.isFinancialProduct,
            hasSignature = input.hasSignature,
            hasPrintedClientReceipt = input.hasPrintedClientReceipt,
            hasWarranty = input.hasWarranty,
            applicationName = input.applicationName,
            interestAmount = input.interestAmount,
            changeAmount = input.changeAmount,
            serviceTax = input.serviceTax,
            cityState = input.cityState,
            hasSentReference = input.hasSentReference,
            v40Code = input.v40Code,
            secondaryProductName = input.secondaryProductName,
            paymentTransactionId = input.paymentTransactionId,
            avaiableBalance = input.avaiableBalance,
            pan = input.pan,
            originalTransactionId = input.originalTransactionId,
            originalTransactionDate = input.originalTransactionDate,
            secondaryProductCode = input.secondaryProductCode,
            hasSentMerchantCode = input.hasSentMerchantCode,
            documentType = input.documentType,
            statusCode = input.statusCode,
            merchantAddress = input.merchantAddress,
            merchantCode = input.merchantCode,
            paymentTypeCode = input.paymentTypeCode,
            hasConnectivity = input.hasConnectivity,
            productName = input.productName,
            merchantName = input.merchantName,
            entranceMode = input.entranceMode,
            firstQuotaAmount = input.firstQuotaAmount,
            cardCaptureType = input.cardCaptureType,
            totalizerCode = input.totalizerCode,
            requestDate = input.requestDate,
            boardingTax = input.boardingTax,
            applicationId = input.applicationId,
            numberOfQuotas = input.numberOfQuotas,
            document = input.document
        )
    }
}
