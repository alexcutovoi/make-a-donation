package com.app.makeadonation.payment

import android.net.Uri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object PaymentDispatcher {
    private val _results = MutableSharedFlow<Uri>(
        extraBufferCapacity = 1
    )
    val results = _results.asSharedFlow()

    fun dispatch(data: Uri) {
        _results.tryEmit(data)
    }
}