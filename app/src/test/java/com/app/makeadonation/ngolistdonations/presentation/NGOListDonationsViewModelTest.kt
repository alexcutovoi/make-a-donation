package com.app.makeadonation.ngolistdonations.presentation

import android.content.Context
import android.net.Uri
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.CoroutinesTestRule
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.ngolistdonations.domain.usecase.NgoListDonationsUseCase
import com.app.makeadonation.ngolistdonations.mocks.MockUtils
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
class NGOListDonationsViewModelTest : KoinTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val textProvider: TextProvider = mockk(relaxed = true)
    private val useCase: NgoListDonationsUseCase = mockk(relaxed = true)
    private val paymentFlow = MutableSharedFlow<Uri>()
    private val mockContext: Context = mockk(relaxed = true)
    private val paymentMockUri: Uri = mockk()
    private val listUri: Uri = mockk()

    private val viewModel: NGOListDonationsViewModel by inject()

    @Before
    fun setup() {
        mockkObject(PaymentDispatcher)
        mockkObject(MakeADonationApplication.Companion)
        every { PaymentDispatcher.results } returns paymentFlow
        every { MakeADonationApplication.getApplicationContext() } returns mockContext
        every { mockContext.getString(any()) } returns "Test text"

        startKoin {
            modules(
                module {
                    single<TextProvider> { textProvider }
                    single<NgoListDonationsUseCase> { useCase }
                    factory { NGOListDonationsViewModel(get(), get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        unmockkObject(PaymentDispatcher)
        unmockkObject(MakeADonationApplication.Companion)
        stopKoin()
    }

    @Test
    fun `when start, list only donations stored in server too`() = runTest {
        val donationInfoList = listOf(MockUtils.getDonationInfo())
        coEvery { useCase.listRegisteredDonations(0, 5) } returns flowOf(listUri)
        coEvery { useCase.listRegisteredDonationsFromServer(any()) } returns flowOf(donationInfoList)

        viewModel.init()
        advanceUntilIdle()

        val showLoadingEvent = viewModel.ngoListDonationsChannel.receive()
        assertTrue(showLoadingEvent is BaseEvent.ShowLoading)

        val listDonationsEvent = viewModel.ngoListDonationsChannel.receive()
        assertTrue(listDonationsEvent is NGOListDonationsEvent.ListDonations)
        assertEquals(listUri, (listDonationsEvent as NGOListDonationsEvent.ListDonations).uri)

        viewModel.donations(emptyList())
        advanceUntilIdle()

        val listSuccessDonationsEvent = viewModel.ngoListDonationsChannel.receive()
        assertTrue(listSuccessDonationsEvent is NGOListDonationsEvent.ListOrdersSuccess)
        assertEquals(1, (listSuccessDonationsEvent as NGOListDonationsEvent.ListOrdersSuccess).listDonations.size)

        val hideLoadingEvent = viewModel.ngoListDonationsChannel.receive()
        assertTrue(hideLoadingEvent is BaseEvent.HideLoading)
    }

    @Test
    fun `when select a donation to cancel, operation succeeded`() = runTest {
        coEvery { useCase.listRegisteredDonations(0, 5) } returns flowOf(listUri)
        coEvery { useCase.cancelDonation("donation-1", "cielo456", "auth123", 1000L) } returns flowOf(listUri)
        every { useCase.handlePaymentList(paymentMockUri) } returns PaymentResult.CancelledOrderSuccess(
                PaymentMockUtils.getOperationResponse("CANCELED")
                )
        every { textProvider.getText(R.string.warning) } returns "Aviso"
        every { textProvider.getText(R.string.donation_successfully_canceled) } returns "Doação cancelada com sucesso"

        viewModel.init()
        advanceUntilIdle()

        viewModel.ngoListDonationsChannel.receive()
        viewModel.ngoListDonationsChannel.receive()

        paymentFlow.emit(paymentMockUri)
        advanceUntilIdle()

        viewModel.cancelOrder("donation-1", MockUtils.getDonationInfo())
        advanceUntilIdle()

        val cancelDonationEvent = viewModel.ngoListDonationsChannel.receive()
        assertTrue(cancelDonationEvent is NGOListDonationsEvent.CancelledDonation)
        (cancelDonationEvent as NGOListDonationsEvent.CancelledDonation).run {
            assertEquals(title, "Aviso")
            assertEquals(description, "Doação cancelada com sucesso")
        }
    }

    @Test
    fun `when user will cancel donation, user gives up the operation`() = runTest {
        val errorResponse = ErrorResponse(code = 1, reason = "CANCELADO PELO USUÁRIO")

        coEvery { useCase.listRegisteredDonations(0, 5) } returns flowOf(listUri)
        every { useCase.handlePaymentList(paymentMockUri) } returns PaymentResult.Cancel(errorResponse)

        viewModel.init()
        advanceUntilIdle()

        viewModel.ngoListDonationsChannel.receive()
        viewModel.ngoListDonationsChannel.receive()

        paymentFlow.emit(paymentMockUri)
        advanceUntilIdle()

        val event = viewModel.ngoListDonationsChannel.receive()
        assertTrue(event is NGOListDonationsEvent.PaymentError)
        assertEquals("CANCELADO PELO USUÁRIO", (event as NGOListDonationsEvent.PaymentError).description)
    }

    @Test
    fun `when user will cancel donation, operation has an error`() = runTest {
        val errorResponse = ErrorResponse(code = 2, reason = "Falha no processo de pagamento")

        coEvery { useCase.listRegisteredDonations(0, 5) } returns flowOf(listUri)
        every { useCase.handlePaymentList(paymentMockUri) } returns PaymentResult.Error(errorResponse)

        viewModel.init()
        advanceUntilIdle()

        viewModel.ngoListDonationsChannel.receive()
        viewModel.ngoListDonationsChannel.receive()

        paymentFlow.emit(paymentMockUri)
        advanceUntilIdle()

        val event = viewModel.ngoListDonationsChannel.receive()
        assertTrue(event is NGOListDonationsEvent.PaymentError)
        assertEquals("Falha no processo de pagamento", (event as NGOListDonationsEvent.PaymentError).description)
    }

    @Test
    fun `when user will cancel donation, operation is succeeded`() = runTest {
        val successResponse = PaymentMockUtils.getOperationResponse("CANCELED")

        coEvery { useCase.listRegisteredDonations(0, 5) } returns flowOf(listUri)
        every { useCase.handlePaymentList(paymentMockUri) } returns PaymentResult.CancelledOrderSuccess(successResponse)

        viewModel.init()
        advanceUntilIdle()

        viewModel.ngoListDonationsChannel.receive()
        viewModel.ngoListDonationsChannel.receive()

        paymentFlow.emit(paymentMockUri)
        advanceUntilIdle()

        val event = viewModel.ngoListDonationsChannel.receive()
        assertTrue(event is NGOListDonationsEvent.CancelledDonation)
    }
}
