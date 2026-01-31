package com.app.makeadonation.ngodonationreceipt.presentation

import androidx.lifecycle.ViewModel
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.sendInViewModelScope
import kotlinx.coroutines.channels.Channel

class NGOReceiptViewModel : ViewModel() {
    private val _ngoReceiptChannel = Channel<BaseEvent>()
    val ngoReceiptChannel = _ngoReceiptChannel

    fun goToHome() {
        _ngoReceiptChannel.sendInViewModelScope(
            this@NGOReceiptViewModel,
            NGOReceiptEvent.GoToHome
        )
    }
}