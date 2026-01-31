package com.app.makeadonation.ngodonationreceipt.presentation

import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory

class NGOReceiptEvent : BaseEvent() {
    data object GoToHome : BaseEvent()
}