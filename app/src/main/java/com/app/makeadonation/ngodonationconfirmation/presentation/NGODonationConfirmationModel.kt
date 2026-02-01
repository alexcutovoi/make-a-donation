package com.app.makeadonation.ngodonationconfirmation.presentation

import androidx.lifecycle.ViewModel
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.Utils
import com.app.makeadonation.common.sendInViewModelScope
import kotlinx.coroutines.channels.Channel

class NGODonationConfirmationModel : ViewModel() {
    private val _ngoReceiptChannel = Channel<BaseEvent>()
    val ngoReceiptChannel = _ngoReceiptChannel

    fun showDonation(ngoName: String, donationValue: Long) {
        _ngoReceiptChannel.sendInViewModelScope(
            this@NGODonationConfirmationModel,
            NGODonationConfirmationEvent.ShowDonationData(
                ngoName,
                Utils.formatCurrency(donationValue)
            )
        )
    }

    fun goToHome() {
        _ngoReceiptChannel.sendInViewModelScope(
            this@NGODonationConfirmationModel,
            NGODonationConfirmationEvent.GoToHome
        )
    }
}