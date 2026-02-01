package com.app.makeadonation.payment

import android.net.Uri
import android.util.Base64
import android.util.Log
import com.app.makeadonation.BuildConfig
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.app.makeadonation.common.Utils
import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.data.model.ItemRequest
import com.app.makeadonation.payment.data.model.ItemResponse
import com.app.makeadonation.payment.data.model.OrderRequest
import com.app.makeadonation.payment.data.model.SuccessResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult
import com.google.gson.Gson
import kotlin.String

object PaymentCoordinator {
    private const val PAY_ACTION ="payment"
    private const val PAY_REVERSAL_ACTION = "payment-reversal"
    private const val LIO_SCHEMA = "lio"
    private const val RESPONSE = "response"
    private var returnHost = ""
    private var returnSchema = ""

    init {
        MakeADonationApplication.getApplicationContext().run {
            returnHost = getString(R.string.return_host)
            returnSchema = getString(R.string.return_schema)
        }
    }

    fun createOrderRequest(value: Long): Uri {
        val orderRequest = OrderRequest(
            BuildConfig.CREDENTIALS_ACCESS_TOKEN,
            BuildConfig.CREDENTIALS_CLIENT_ID,
            email = "asd@aol.com",
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

        return Uri.Builder()
            .scheme(LIO_SCHEMA)
            .authority(PAY_ACTION)
            .appendQueryParameter(
                "request",
                orderToBase64(orderRequest)
            )
            .appendQueryParameter(
                "urlCallback",
                "$returnSchema://$returnHost"
            )
            .build()
    }

    fun getPaymentResponse(data: Uri): PaymentResult {
        val result = data.getQueryParameter(RESPONSE)?.let {
            Base64.decode(it, Base64.DEFAULT).decodeToString()
        } ?: throw IllegalArgumentException("Invalid payment response")

        // Every order has at lease 1 item
        return if(Utils.hasField("items", result)) {
            PaymentResult.Success(
                Utils.retrieveObject(result, SuccessResponse::class.java)
            )
        } else {
            val error = Utils.retrieveObject(result, ErrorResponse::class.java)
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

    private fun orderToBase64(order: OrderRequest): String {
        val data = Gson().toJson(order).toString().toByteArray(Charsets.UTF_8)

        return Base64.encodeToString(data, Base64.DEFAULT)
    }
}

/*DEBITO_AVISTA
DEBITO_PAGTO_FATURA_DEBITO
CREDITO_AVISTA
CREDITO_PARCELADO_LOJA
CREDITO_PARCELADO_ADM
CREDITO_PARCELADO_BNCO
PRE_AUTORIZACAO
CREDITO_PARCELADO_CLIENTE
CREDITO_CREDIARIO_CREDITO
VOUCHER_ALIMENTACAO
VOUCHER_REFEICAO
VOUCHER_AUTOMOTIVO
VOUCHER_CULTURA
VOUCHER_PEDAGIO
VOUCHER_BENEFICIOS
VOUCHER_AUTO
VOUCHER_CONSULTA_SALDO
VOUCHER_VALE_PEDAGIO
CREDIARIO_VENDA
CREDIARIO_SIMULACAO
CARTAO_LOJA_AVISTA
CARTAO_LOJA_PARCELADO_LOJA
CARTAO_LOJA_PARCELADO
CARTAO_LOJA_PARCELADO_BANCO
CARTAO_LOJA_PAGTO_FATURA_CHEQUE
CARTAO_LOJA_PAGTO_FATURA_DINHEIRO
FROTAS
PIX*/