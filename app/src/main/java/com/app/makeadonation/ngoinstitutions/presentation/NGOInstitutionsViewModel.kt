package com.app.makeadonation.ngoinstitutions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgoInstitutionsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NGOInstitutionsViewModel(
    private val ngpInstitutionsUseCase: NgoInstitutionsUseCase
) : ViewModel() {
    private val _ngoInstitutionsChannel = Channel<BaseEvent>()
    val ngoInstitutionsChannel = _ngoInstitutionsChannel

    fun init(ngoCategoryId: Int) {
        retrieveCategories(ngoCategoryId)
    }

    fun donate(donationValue: Long)  = viewModelScope.launch {
        ngpInstitutionsUseCase.donate(donationValue)
            .collectLatest { uri ->
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    NGOInstitutionsEvent.PaymentOrder(uri)
                )
            }
    }

    private fun retrieveCategories(ngoCategoryId: Int) = viewModelScope.launch {
        ngpInstitutionsUseCase.retrieveNGOs(ngoCategoryId)
            .collectLatest  {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                        NGOInstitutionsEvent.Institutions(it)
                )
            }
    }
}