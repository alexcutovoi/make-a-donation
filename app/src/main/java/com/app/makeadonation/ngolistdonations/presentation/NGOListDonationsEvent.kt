package com.app.makeadonation.ngolistdonations.presentation

import android.net.Uri
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.payment.domain.entity.ListOrders

class NGOListDonationsEvent : BaseEvent() {
    data class ListDonations(val uri: Uri) : BaseEvent()
    data class ListOrdersSuccess(val listOrders: ListOrders) : BaseEvent()
    data class PaymentCancelled(val title: String, val description: String) : BaseEvent()
    data class PaymentError(val title: String, val description: String) : BaseEvent()
}