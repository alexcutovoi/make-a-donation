package com.app.makeadonation.payment

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.app.makeadonation.Utils
import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult
import com.app.makeadonation.payment.mocks.MockUtils
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30], application = android.app.Application::class)
class PaymentCoordinatorTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        mockkObject(MakeADonationApplication.Companion)
        every { MakeADonationApplication.getApplicationContext() } returns context
    }

    @After
    fun tearDown() {
        unmockkObject(MakeADonationApplication.Companion)
    }

    @Test
    fun `when creates payment uri request, checks params for request`() {
        val donationValue = 10000L
        val uri = PaymentCoordinator.createOrderRequest(donationValue)
        val scheme = "lio"
        val action = "payment"
        val queryParameter = "request"
        val request = uri.getQueryParameter(queryParameter)

        assertNotNull(request)
        assertEquals(scheme, uri.scheme)
        assertEquals(action, uri.authority)
    }

    @Test
    fun `when creates payment uri request, checks params for response`() {
        val queryParameter = "urlCallback"
        val uri = PaymentCoordinator.createOrderRequest(10000L)
        val returnScheme = context.getString(R.string.return_scheme)
        val returnHost = context.getString(R.string.return_host)
        val callback = uri.getQueryParameter(queryParameter)

        assertNotNull(callback)
        assertEquals("$returnScheme://$returnHost", callback)
    }

    @Test
    fun `listPayments should return uri with lio scheme and orders authority`() {
        val uri = PaymentCoordinator.listPayments(0, 5)
        val scheme = "lio"
        val action = "orders"
        val queryParameter = "request"
        val request = uri.getQueryParameter(queryParameter)
        val data = Utils.encodeToBase64(MockUtils.getListOrders(0, 5))

        assertNotNull(request)

        assertEquals(scheme, uri.scheme)
        assertEquals(action, uri.authority)
        assertEquals(data, request)
    }

    @Test
    fun `when list donations, check how many items will be displayed and number items received`() {
        val uri = PaymentCoordinator.listPayments(0, 10)
        val scheme = "lio"
        val action = "orders"
        val queryParameter = "request"
        val request = uri.getQueryParameter(queryParameter)
        val decoded = Utils.decodeFromBase64(uri, queryParameter)

        assertNotNull(decoded)
        assertNotNull(request)
        assertEquals(scheme, uri.scheme)
        assertEquals(action, uri.authority)
        assertTrue(decoded!!.contains("\"pageSize\":5") || decoded.contains("\"numberOfItems\":5"))
    }

    @Test
    fun `cancelOrder should return uri with lio scheme and payment-reversal authority`() {
        val scheme = "lio"
        val action = "payment-reversal"
        val uri = PaymentCoordinator.cancelOrder("id-1", "cielo123", "auth456", 5000L)

        assertEquals(scheme, uri.scheme)
        assertEquals(action, uri.authority)
        assertNotNull(uri.getQueryParameter("request"))
        assertNotNull(uri.getQueryParameter("urlCallback"))
    }

    @Test
    fun `getPaymentResponse should return Success when response has items field`() {
        val successResponse = Utils.transformToJson(MockUtils.getOperationResponse("PAID"))
        val uri = buildResponseUri(successResponse)

        val result = PaymentCoordinator.getPaymentResponse(uri)

        assertTrue(result is PaymentResult.Success)
        val success = result as PaymentResult.Success
        assertEquals("order-1", success.response.id)
    }

    @Test
    fun `getPaymentResponse should return Cancel when error code is 1`() {
        val errorJson = Utils.transformToJson(ErrorResponse(code = 1, reason = "User cancelled"))
        val uri = buildResponseUri(errorJson)

        val result = PaymentCoordinator.getPaymentResponse(uri)

        assertTrue(result is PaymentResult.Cancel)
        assertEquals("User cancelled", (result as PaymentResult.Cancel).response.reason)
    }

    @Test
    fun `getPaymentResponse should return Error when error code is 2`() {
        val errorJson = Utils.transformToJson(ErrorResponse(code = 2, reason = "Transaction failed"))
        val uri = buildResponseUri(errorJson)

        val result = PaymentCoordinator.getPaymentResponse(uri)

        assertTrue(result is PaymentResult.Error)
        assertEquals("Transaction failed", (result as PaymentResult.Error).response.reason)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getPaymentResponse should throw when response parameter is missing`() {
        val uri = Uri.parse("returnscheme://returnhost")
        PaymentCoordinator.getPaymentResponse(uri)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getPaymentResponse should throw when error code is unknown`() {
        val errorJson = Utils.transformToJson(ErrorResponse(code = 99, reason = "Unknown error"))
        val uri = buildResponseUri(errorJson)

        PaymentCoordinator.getPaymentResponse(uri)
    }

    @Test
    fun `getPaymentList should return ListOrdersSuccess when response has totalItems field`() {
        val listOrdersJson = Utils.transformToJson(MockUtils.getListOrdersResponse(0, listOf(), 0, 1))
        val uri = buildResponseUri(listOrdersJson)

        val result = PaymentCoordinator.getPaymentList(uri)

        assertTrue(result is PaymentResult.ListOrdersSuccess)
    }

    @Test
    fun `getPaymentList should return CancelledOrderSuccess when response has status field`() {
        val successJson = Utils.transformToJson(MockUtils.getOperationResponse("CANCELED"))
        val uri = buildResponseUri(successJson)

        val result = PaymentCoordinator.getPaymentList(uri)

        assertTrue(result is PaymentResult.CancelledOrderSuccess)
    }

    @Test
    fun `getPaymentList should return Cancel when error code is 1`() {
        val errorJson = Utils.transformToJson(ErrorResponse(code = 1, reason = "Cancelled"))
        val uri = buildResponseUri(errorJson)

        val result = PaymentCoordinator.getPaymentList(uri)

        assertTrue(result is PaymentResult.Cancel)
    }

    @Test
    fun `getPaymentList should return Error when error code is 2`() {
        val errorJson = Utils.transformToJson(ErrorResponse(code = 2, reason = "Error"))
        val uri = buildResponseUri(errorJson)

        val result = PaymentCoordinator.getPaymentList(uri)

        assertTrue(result is PaymentResult.Error)
    }

    private fun buildResponseUri(jsonResponse: String): Uri {
        val returnScheme = "returnscheme"
        val returnHost = "returnhost"
        val response = "response"
        val encoded = Base64.encodeToString(jsonResponse.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        return Uri.Builder()
            .scheme(returnScheme)
            .authority(returnHost)
            .appendQueryParameter(response, encoded)
            .build()
    }
}
