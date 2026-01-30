package com.app.makeadonation.common

abstract class BaseEvent {
    data object ShowLoading : BaseEvent()
    data object HideLoading : BaseEvent()
    data object Finish : BaseEvent()
}