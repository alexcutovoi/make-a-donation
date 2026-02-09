package com.app.makeadonation.ngodonationconfirmation.presentation

import com.app.makeadonation.Utils
import com.app.makeadonation.common.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class NGODonationConfirmationModelTest : KoinTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val viewModel: NGODonationConfirmationModel by inject()

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    factory { NGODonationConfirmationModel() }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when donation is confirmed, show donation data`() = runTest {
        val ngoName = "Enviromental NGO"
        val donationValue = 1000L

        viewModel.showDonation(ngoName, donationValue)
        coroutinesTestRule.testDispatcher.scheduler.advanceUntilIdle()

        val event = viewModel.ngoReceiptChannel.receive()

        assertTrue(event is NGODonationConfirmationEvent.ShowDonationData)
        val showDonationEvent = event as NGODonationConfirmationEvent.ShowDonationData
        assertEquals(ngoName, showDonationEvent.ngoName)
        assertEquals(Utils.formatCurrency(donationValue), showDonationEvent.donationValue)
    }

    @Test
    fun `when event is GoToHome, send user to home`() = runTest {
        viewModel.goToHome()
        coroutinesTestRule.testDispatcher.scheduler.advanceUntilIdle()

        val event = viewModel.ngoReceiptChannel.receive()

        assertTrue(event is NGODonationConfirmationEvent.GoToHome)
    }
}
