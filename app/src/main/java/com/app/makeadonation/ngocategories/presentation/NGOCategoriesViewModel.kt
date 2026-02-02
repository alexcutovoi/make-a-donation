package com.app.makeadonation.ngocategories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngocategories.domain.usecase.NgoCategoriesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NGOCategoriesViewModel(
    private val ngpCategoriesUseCase: NgoCategoriesUseCase
) : ViewModel() {
    private val _ngoCategoriesChannel = Channel<BaseEvent>()
    val ngoCategoriesChannel = _ngoCategoriesChannel

    fun init() {
        retrieveCategories()
    }

    private fun retrieveCategories() = viewModelScope.launch {
        ngpCategoriesUseCase.retrieveCategories()
            .onStart {
                _ngoCategoriesChannel.sendInViewModelScope(
                    this@NGOCategoriesViewModel,
                    BaseEvent.ShowLoading
                )
            }
            .onCompletion {
                _ngoCategoriesChannel.sendInViewModelScope(
                    this@NGOCategoriesViewModel,
                    BaseEvent.HideLoading
                )
            }
            .collectLatest  {
                _ngoCategoriesChannel.sendInViewModelScope(
                    this@NGOCategoriesViewModel,
                        NGOCategoriesEvent.Categories(it)
                )
            }
    }
}