package com.app.makeadonation.ngocategories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.common.onException
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngocategories.domain.usecase.NgoCategoriesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NGOCategoriesViewModel(
    private val ngpCategoriesUseCase: NgoCategoriesUseCase,
    private val textProvider: TextProvider
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
            .onException {
                _ngoCategoriesChannel.sendInViewModelScope(
                    this@NGOCategoriesViewModel,
                    NGOCategoriesEvent.EmptyCategories(
                        textProvider.getText(R.string.warning),
                        textProvider.getText(R.string.donate_categories_empty_list_error)
                    )
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