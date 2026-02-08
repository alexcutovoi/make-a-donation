package com.app.makeadonation.payment

import android.net.Uri
import com.app.makeadonation.BuildConfig
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.app.makeadonation.common.Utils
import com.app.makeadonation.payment.data.model.DonationCancellationRequest
import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.data.model.ItemRequest
import com.app.makeadonation.payment.data.model.ListOrdersRequest
import com.app.makeadonation.payment.data.model.ListOrdersResponse
import com.app.makeadonation.payment.data.model.OrderRequest
import com.app.makeadonation.payment.data.model.SuccessResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlin.String

object PaymentCoordinator {
    private const val PAY_ACTION ="payment"
    private const val PAY_REVERSAL_ACTION = "payment-reversal"
    private const val LIO_SCHEMA = "lio"
    private const val REQUEST = "request"
    private const val RESPONSE = "response"
    private const val LIST_ORDERS = "orders"
    private const val URI_CALLBACK = "urlCallback"
    private var returnHost = ""
    private var returnScheme = ""

    init {
        MakeADonationApplication.getApplicationContext().run {
            returnHost = getString(R.string.return_host)
            returnScheme = getString(R.string.return_scheme)
        }
    }

    fun createOrderRequest(value: Long): Uri {
        val orderRequest = OrderRequest(
            BuildConfig.CREDENTIALS_ACCESS_TOKEN,
            BuildConfig.CREDENTIALS_CLIENT_ID,
            installments = 1,
            items = mutableListOf(
                ItemRequest(
                    name = "",
                    quantity = 1,
                    sku = generateSKU(),
                    unitOfMeasure = "valor",
                    unitPrice = value
                )
            ),
            paymentCode = "",
            value = value.toString(),
            reference = "",
            merchantCode = null
        )

        return createUri(PAY_ACTION, Utils.encodeToBase64(orderRequest))
    }

    fun listPayments(page:Int, items: Int): Uri {
        val maxNumOfItems = 5
        val numOfItems = items.takeIf {
            it <= maxNumOfItems
        } ?: maxNumOfItems

        val listOrdersRequest = ListOrdersRequest(
            page,
            numOfItems,
            BuildConfig.CREDENTIALS_ACCESS_TOKEN,
            BuildConfig.CREDENTIALS_CLIENT_ID,
        )
        return createUri(LIST_ORDERS, Utils.encodeToBase64(listOrdersRequest))
    }

    fun cancelOrder(id: String, cieloCode: String, authCode: String, value: Long): Uri {
        val cancelOrderRequest = DonationCancellationRequest(
            id,
            BuildConfig.CREDENTIALS_ACCESS_TOKEN,
            BuildConfig.CREDENTIALS_CLIENT_ID,
            cieloCode,
            authCode,
            value
        )
        return createUri(PAY_REVERSAL_ACTION, Utils.encodeToBase64(cancelOrderRequest))
    }

    fun getPaymentList(data: Uri): PaymentResult {
        val result = Utils.decodeFromBase64(data, RESPONSE) ?:
        throw IllegalArgumentException("Invalid payment response")

        return if(Utils.hasField("totalItems", result)) {
            PaymentResult.ListOrdersSuccess(
                Utils.retrieveObject<ListOrdersResponse>(result)
            )
        } else if(Utils.hasField("status", result)) {
                PaymentResult.CancelledOrderSuccess(
                    Utils.retrieveObject<SuccessResponse>(result)
                )
        } else {
            val error = Utils.retrieveObject<ErrorResponse>(result)
            val cancelledCode = 1
            val errorCode = 2

            return when (error.code) {
                cancelledCode -> {
                    PaymentResult.Cancel(error)
                }
                errorCode -> {
                    PaymentResult.Error(error)
                }
                else -> {
                    throw IllegalArgumentException("Invalid payment response")
                }
            }
        }
    }

    fun getPaymentResponse(data: Uri): PaymentResult {
        val result = Utils.decodeFromBase64(data, RESPONSE) ?:
        throw IllegalArgumentException("Invalid payment response")

        // Every order has at lease 1 item
        return if(Utils.hasField("items", result)) {
            PaymentResult.Success(
                Utils.retrieveObject<SuccessResponse>(result)
            )
        } else {
            val error = Utils.retrieveObject<ErrorResponse>(result)
            val cancelledCode = 1
            val errorCode = 2

            return when (error.code) {
                cancelledCode -> {
                    PaymentResult.Cancel(error)
                }
                errorCode -> {
                    PaymentResult.Error(error)
                }
                else -> {
                    throw IllegalArgumentException("Invalid payment response")
                }
            }
        }
    }

    private fun generateSKU(): String {
        val length = 5

        val letters = (1..length).map {
            ('A'..'z').random()
        }.joinToString("")

        val numbers = (1..length).joinToString("") {
            (1..50).random().toString()
        }

        return "$letters$numbers"
    }

    private fun createUri(action: String, data: String): Uri {
        return Uri.Builder()
            .scheme(LIO_SCHEMA)
            .authority(action)
            .appendQueryParameter(
                REQUEST,
                data
            )
            .appendQueryParameter(
                URI_CALLBACK,
                "$returnScheme://$returnHost"
            )
            .build()
    }
}
