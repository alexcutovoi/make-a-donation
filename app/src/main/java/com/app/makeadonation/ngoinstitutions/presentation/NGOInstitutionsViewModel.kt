package com.app.makeadonation.ngoinstitutions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.onException
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgoInstitutionsUseCase
import com.app.makeadonation.payment.PaymentDispatcher
import com.app.makeadonation.payment.data.mapper.ErrorResponseMapper
import com.app.makeadonation.payment.data.mapper.SuccessResponseMapper
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NGOInstitutionsViewModel(
    private val ngpInstitutionsUseCase: NgoInstitutionsUseCase
) : ViewModel() {
    private val _ngoInstitutionsChannel = Channel<BaseEvent>()
    val ngoInstitutionsChannel = _ngoInstitutionsChannel

    private lateinit var selectedNgo: NgoInfo

    fun init(ngoCategoryId: Int) {
        retrieveCategories(ngoCategoryId)
        handlePayment()
    }

    fun donate(selectedNgo: NgoInfo, donationValue: Long)  = viewModelScope.launch {
        this@NGOInstitutionsViewModel.selectedNgo = selectedNgo

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

    private fun handlePayment() = viewModelScope.launch {
        PaymentDispatcher.results
            .onException {

            }
            .collectLatest { data ->
                when (val result = ngpInstitutionsUseCase.handlePayment(data)) {
                    is PaymentResult.Success ->
                        _ngoInstitutionsChannel.sendInViewModelScope(
                            this@NGOInstitutionsViewModel,
                            NGOInstitutionsEvent.PaymentSuccess (
                                selectedNgo,
                                SuccessResponseMapper().generate(result.response)
                            )
                        )

                    is PaymentResult.Cancel ->
                        _ngoInstitutionsChannel.sendInViewModelScope(
                            this@NGOInstitutionsViewModel,
                            NGOInstitutionsEvent.PaymentError (
                                "Atenção", ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    is PaymentResult.Error ->
                        _ngoInstitutionsChannel.sendInViewModelScope(
                            this@NGOInstitutionsViewModel,
                            NGOInstitutionsEvent.PaymentError (
                                "Atenção", ErrorResponseMapper().generate(result.response).reason
                            )
                        )
                }
            }
    }
}