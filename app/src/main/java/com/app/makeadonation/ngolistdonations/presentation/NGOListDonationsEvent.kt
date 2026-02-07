package com.app.makeadonation.ngolistdonations.presentation

import android.net.Uri
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.ngolistdonations.domain.entity.NgoDonationInfo

class NGOListDonationsEvent : BaseEvent() {
    data class CancelDonation(val uri: Uri) : BaseEvent()
    data class CancelledDonation(val title: String, val description: String) : BaseEvent()
    data class ListDonations(val uri: Uri) : BaseEvent()
    data class ListOrdersSuccess(val listDonations: List<NgoDonationInfo>) : BaseEvent()
    data class PaymentCancelled(val title: String, val description: String) : BaseEvent()
    data class PaymentError(val title: String, val description: String) : BaseEvent()
}