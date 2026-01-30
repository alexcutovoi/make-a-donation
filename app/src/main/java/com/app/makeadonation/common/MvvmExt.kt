package com.app.makeadonation.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T, V: ViewModel> Channel<T>.sendInViewModelScope(scope: V, vararg event: T) {
    scope.viewModelScope.launch {
        event.forEach {
            this@sendInViewModelScope.send(it)
        }
    }
}

fun <T> LifecycleOwner.observe(flow: Flow<T>, action: (t:T) -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect{ action(it) }
        }
    }
}