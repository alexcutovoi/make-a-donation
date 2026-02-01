package com.app.makeadonation.ngoinstitutions.presentation

import android.net.Uri
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.payment.domain.entity.Success

class NGOInstitutionsEvent : BaseEvent() {
    data class Institutions(val institutions: List<NgoInfo>) : BaseEvent()
    data class PaymentOrder(val uri: Uri) : BaseEvent()
    data class PaymentSuccess(val selectedNgo: NgoInfo, val success: Success) : BaseEvent()
    data class PaymentCancelled(val title: String, val description: String) : BaseEvent()
    data class PaymentError(val title: String, val description: String) : BaseEvent()
}