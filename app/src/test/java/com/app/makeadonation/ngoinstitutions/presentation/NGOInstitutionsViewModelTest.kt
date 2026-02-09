package com.app.makeadonation.ngoinstitutions.presentation

import android.net.Uri
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.CoroutinesTestRule
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgoInstitutionsUseCase
import com.app.makeadonation.ngoinstitutions.mocks.MockUtils
import com.app.makeadonation.payment.PaymentDispatcher
import com.app.makeadonation.payment.data.model.ErrorResponse
import com.app.makeadonation.payment.domain.entity.PaymentResult
import com.app.makeadonation.payment.mocks.MockUtils as PaymentMockUtils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class NGOInstitutionsViewModelTest : KoinTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val textProvider: TextProvider = mockk(relaxed = true)
    private val useCase: NgoInstitutionsUseCase = mockk(relaxed = true)
    private val paymentFlow = MutableSharedFlow<Uri>()
    private val mockUri: Uri = mockk()

    private val viewModel: NGOInstitutionsViewModel by inject()

    @Before
    fun setup() {
        mockkObject(PaymentDispatcher)
        every { PaymentDispatcher.results } returns paymentFlow

        startKoin {
            modules(
                module {
                    single<TextProvider> { textProvider }
                    single<NgoInstitutionsUseCase> { useCase }
                    factory { NGOInstitutionsViewModel(get(), get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        unmockkObject(PaymentDispatcher)
        stopKoin()
    }

    @Test
    fun `init should emit ShowLoading then Institutions then HideLoading`() = runTest {
        val ngoCategoryId = 1
        val institutions = listOf(MockUtils.getNgoInfo())
        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(institutions)

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        val showLoadingEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(showLoadingEvent is BaseEvent.ShowLoading)

        val ionstitutionsEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(ionstitutionsEvent is NGOInstitutionsEvent.Institutions)
        assertEquals(1, (ionstitutionsEvent as NGOInstitutionsEvent.Institutions).institutions.size)

        val hideLoadingEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(hideLoadingEvent is BaseEvent.HideLoading)
    }

    @Test
    fun `donate should emit PaymentOrder with uri`() = runTest {
        val ngoCategoryId = 1
        val donationValue = 1000L

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        coEvery { useCase.donate(donationValue) } returns flowOf(mockUri)

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        viewModel.donate(MockUtils.getNgoInfo(), donationValue)
        advanceUntilIdle()

        val paymentOrderEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentOrderEvent is NGOInstitutionsEvent.PaymentOrder)
        assertEquals(mockUri, (paymentOrderEvent as NGOInstitutionsEvent.PaymentOrder).uri)
    }

    @Test
    fun `donate should emit PaymentError when useCase throws`() = runTest {
        val ngoCategoryId = 1
        val donationValue = 1000L

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        coEvery { useCase.donate(donationValue) } returns flow {
            throw RuntimeException("Payment failed")
        }

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        viewModel.donate(MockUtils.getNgoInfo(), donationValue)
        advanceUntilIdle()

        val paymentErrorEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentErrorEvent is NGOInstitutionsEvent.PaymentError)
    }

    @Test
    fun `handlePayment should emit PaymentSuccess on Success result`() = runTest {
        val ngoCategoryId = 1
        val donationValue = 1000L
        val successResponse = PaymentMockUtils.getOperationResponse("PAID")

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        every { useCase.handlePayment(mockUri) } returns PaymentResult.Success(successResponse)
        coEvery { useCase.storeDonation(
            any(), any(), any(), any(), any(), any(), any())
        } returns flowOf(Unit)

        coEvery { useCase.donate(donationValue) } returns flowOf(mockUri)
        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        viewModel.donate(MockUtils.getNgoInfo(), donationValue)
        advanceUntilIdle()
        viewModel.ngoInstitutionsChannel.receive()

        paymentFlow.emit(mockUri)
        advanceUntilIdle()

        val paymentSuccessEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentSuccessEvent is NGOInstitutionsEvent.PaymentSuccess)
        val success = paymentSuccessEvent as NGOInstitutionsEvent.PaymentSuccess
        assertEquals(MockUtils.getNgoInfo(), success.selectedNgo)
        assertEquals(donationValue, success.donationValue)
    }

    @Test
    fun `handlePayment should emit PaymentError on Cancel result`() = runTest {
        val ngoCategoryId = 1
        val errorResponse = ErrorResponse(code = 1, reason = "User cancelled")

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        every { useCase.handlePayment(mockUri) } returns PaymentResult.Cancel(errorResponse)

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        paymentFlow.emit(mockUri)
        advanceUntilIdle()

        val paymentErrorEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentErrorEvent is NGOInstitutionsEvent.PaymentError)
    }

    @Test
    fun `handlePayment should emit PaymentError on Error result`() = runTest {
        val ngoCategoryId = 1
        val errorResponse = ErrorResponse(code = 2, reason = "Transaction failed")

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        every { useCase.handlePayment(mockUri) } returns PaymentResult.Error(errorResponse)

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        paymentFlow.emit(mockUri)
        advanceUntilIdle()

        val paymentErrorEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentErrorEvent is NGOInstitutionsEvent.PaymentError)
    }

    @Test
    fun `tryAgain should retry donate when selectedNgo and valueToDonate are set`() = runTest {
        val ngoCategoryId = 1
        val donationValue = 1000L

        coEvery { useCase.retrieveNGOs(ngoCategoryId) } returns flowOf(emptyList())
        coEvery { useCase.donate(donationValue) } returns flowOf(mockUri)

        viewModel.init(ngoCategoryId)
        advanceUntilIdle()

        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()
        viewModel.ngoInstitutionsChannel.receive()

        viewModel.donate(MockUtils.getNgoInfo(), donationValue)
        advanceUntilIdle()
        viewModel.ngoInstitutionsChannel.receive()

        viewModel.tryAgain()
        advanceUntilIdle()

        val paymentOrderEvent = viewModel.ngoInstitutionsChannel.receive()
        assertTrue(paymentOrderEvent is NGOInstitutionsEvent.PaymentOrder)
    }
}
