package com.app.makeadonation.ngodonationconfirmation.presentation

import com.app.makeadonation.common.BaseEvent

class NGODonationConfirmationEvent : BaseEvent() {
    data class ShowDonationData(val ngoName: String, val donationValue: String) : BaseEvent()
    data object GoToHome : BaseEvent()
}