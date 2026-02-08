package com.app.makeadonation.payment.mocks

import com.app.makeadonation.BuildConfig
import com.app.makeadonation.payment.data.model.ItemRequest
import com.app.makeadonation.payment.data.model.ItemResponse
import com.app.makeadonation.payment.data.model.ListOrdersRequest
import com.app.makeadonation.payment.data.model.ListOrdersResponse
import com.app.makeadonation.payment.data.model.OrderRequest
import com.app.makeadonation.payment.data.model.PaymentFieldsResponse
import com.app.makeadonation.payment.data.model.PaymentResponse
import com.app.makeadonation.payment.data.model.SuccessResponse

object MockUtils {
    fun getOrder(donationValue: Long): OrderRequest {

        return OrderRequest(
            BuildConfig.CREDENTIALS_ACCESS_TOKEN,
            BuildConfig.CREDENTIALS_CLIENT_ID,
            installments = 1,
            items = mutableListOf(
                ItemRequest(
                    name = "",
                    quantity = 1,
                    sku = "abc123",
                    unitOfMeasure = "valor",
                    unitPrice = donationValue
                )
            ),
            paymentCode = "",
            value = donationValue.toString(),
            reference = "",
            merchantCode = null
        )
    }

    fun getListOrders(page: Int, numOfItems: Int) = ListOrdersRequest(
        page,
        numOfItems,
        BuildConfig.CREDENTIALS_ACCESS_TOKEN,
        BuildConfig.CREDENTIALS_CLIENT_ID,
    )

    fun getPaymentFields() = PaymentFieldsResponse(
        isDoubleFontPrintAllowed = "0", hasPassword = "0", primaryProductCode = "1",
        isExternalCall = "1", primaryProductName = "CREDITO", receiptPrintPermission = "1",
        isOnlyIntegrationCancelable = "0", upFrontAmount = "0", creditAdminTax = "0",
        firstQuotaDate = "", isFinancialProduct = "1", hasSignature = "0",
        hasPrintedClientReceipt = "0", hasWarranty = "0", applicationName = "CIELO",
        interestAmount = "0", changeAmount = "0", serviceTax = "0", cityState = "SP",
        hasSentReference = "0", v40Code = "0", secondaryProductName = "",
        paymentTransactionId = "tx1", avaiableBalance = "0", pan = "1234",
        originalTransactionId = "", originalTransactionDate = "",
        secondaryProductCode = "", hasSentMerchantCode = "0", documentType = "",
        statusCode = "1", merchantAddress = "Rua Test", merchantCode = "merchant1",
        paymentTypeCode = "1", hasConnectivity = "1", productName = "CREDITO",
        merchantName = "Test", entranceMode = "1", firstQuotaAmount = "0",
        cardCaptureType = "1", totalizerCode = "1", requestDate = "2026-01-01",
        boardingTax = "0", applicationId = "app1", numberOfQuotas = "1", document = ""
    )

    fun getPayment() = PaymentResponse(
        accessKey = "key", amount = 10000L, applicationName = "app",
        authCode = "auth123", brand = "Visa", cieloCode = "cielo456",
        description = "desc", discountedAmount = 0, externalId = "ext1",
        id = "pay1", installments = 1, mask = "****1234",
        merchantCode = "merchant1", paymentFieldsResponse = getPaymentFields(),
        primaryCode = "primary", requestDate = "2026-01-01",
        secondaryCode = "secondary", terminal = "terminal1"
    )

    fun getItem() = ItemResponse(
        description = "Donation", details = "", id = "item1", name = "Donation",
        quantity = 1, reference = "", sku = "SKU1", unitOfMeasure = "valor", unitPrice = 10000
    )

    fun getOperationResponse(status: String): SuccessResponse {
        return when(status) {
            "PAID" -> { getSuccess() }
            "CANCELED" -> { getCanceled() }
            else -> { getCanceled() }
        }
    }
    fun getListOrdersResponse(page: Int, pageItems: List<SuccessResponse>, totalItems: Int, totalNumPages: Int) = ListOrdersResponse(
        currentPage = page,
        orders = pageItems,
        totalOrdersNum = totalItems,
        totalPagesNum = totalNumPages
    )

    private fun getSuccess() = SuccessResponse(
        createdAt = "Jan 1, 2026 12:00:00 AM", id = "order-1",
        itemsResponse = listOf(getItem()), notes = "", number = "1",
        paidAmount = 10000, paymentsResponse = listOf(getPayment()),
        pendingAmount = 0, price = 10000L, reference = "",
        status = "PAID", type = "PAYMENT", updatedAt = "Jan 1, 2026 12:00:00 AM"
    )

    private fun getCanceled() = SuccessResponse(
        createdAt = "Jan 1, 2026 12:00:00 AM", id = "order-1",
        itemsResponse = listOf(getItem()), notes = "", number = "1",
        paidAmount = 10000, paymentsResponse = listOf(getPayment()),
        pendingAmount = 0, price = 10000L, reference = "",
        status = "CANCELED", type = "PAYMENT", updatedAt = "Jan 1, 2026 12:00:00 AM"
    )
}