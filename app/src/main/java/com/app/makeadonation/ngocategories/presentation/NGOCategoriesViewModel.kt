package com.app.makeadonation.ngocategories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngocategories.domain.usecase.NgoCategoriesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
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
            .collectLatest  {
                _ngoCategoriesChannel.sendInViewModelScope(
                    this@NGOCategoriesViewModel,
                        NGOCategoriesEvent.Categories(it)
                )
            }
    }
}