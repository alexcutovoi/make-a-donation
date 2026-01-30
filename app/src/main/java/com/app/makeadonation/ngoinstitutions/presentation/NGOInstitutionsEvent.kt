package com.app.makeadonation.ngoinstitutions.presentation

import android.net.Uri
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo

class NGOInstitutionsEvent : BaseEvent() {
    data class Institutions(val institutions: List<NgoInfo>) : BaseEvent()
    data class PaymentOrder(val uri: Uri) : BaseEvent()
}