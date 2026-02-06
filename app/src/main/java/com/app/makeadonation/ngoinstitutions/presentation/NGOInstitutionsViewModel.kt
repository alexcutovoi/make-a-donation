package com.app.makeadonation.ngoinstitutions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.common.onException
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgoInstitutionsUseCase
import com.app.makeadonation.payment.PaymentDispatcher
import com.app.makeadonation.payment.data.mapper.ErrorResponseMapper
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NGOInstitutionsViewModel(
    private val textProvider: TextProvider,
    private val ngpInstitutionsUseCase: NgoInstitutionsUseCase
) : ViewModel() {
    private val _ngoInstitutionsChannel = Channel<BaseEvent>()
    val ngoInstitutionsChannel = _ngoInstitutionsChannel

    private var selectedNgo: NgoInfo? = null
    private var valueToDonate: Long? = null

    fun init(ngoCategoryId: Int) {
        retrieveCategories(ngoCategoryId)
        handlePayment()
    }

    fun donate(ngoToDonate: NgoInfo, donationValue: Long)  = viewModelScope.launch {
        selectedNgo = ngoToDonate
        valueToDonate = donationValue

        ngpInstitutionsUseCase.donate(donationValue)
            .onException {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    NGOInstitutionsEvent.PaymentError (
                        textProvider.getText(R.string.warning),
                        textProvider.getText(R.string.donation_try_again)
                    )
                )
            }
            .collectLatest { uri ->
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    NGOInstitutionsEvent.PaymentOrder(uri)
                )
            }
    }

    fun tryAgain() {
        if(selectedNgo != null && valueToDonate != null) {
            donate(selectedNgo!!, valueToDonate!!)
        }
    }

    private fun retrieveCategories(ngoCategoryId: Int) = viewModelScope.launch {
        ngpInstitutionsUseCase.retrieveNGOs(ngoCategoryId)
            .onStart {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    BaseEvent.ShowLoading
                )
            }
            .onCompletion {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    BaseEvent.HideLoading
                )
            }
            .collectLatest  {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                        NGOInstitutionsEvent.Institutions(it)
                )
            }
    }

    private fun handlePayment() = viewModelScope.launch {
        PaymentDispatcher.results
            .collectLatest { data ->
                when (val result = ngpInstitutionsUseCase.handlePayment(data)) {
                    is PaymentResult.Success ->
                        selectedNgo?.let {
                            storeDonation(
                                result.response.price,
                                result.response.id,
                                it
                            )
                        }

                    is PaymentResult.Cancel ->
                        _ngoInstitutionsChannel.sendInViewModelScope(
                            this@NGOInstitutionsViewModel,
                            NGOInstitutionsEvent.PaymentError (
                                textProvider.getText(R.string.warning),
                                ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    is PaymentResult.Error ->
                        _ngoInstitutionsChannel.sendInViewModelScope(
                            this@NGOInstitutionsViewModel,
                            NGOInstitutionsEvent.PaymentError (
                                textProvider.getText(R.string.warning),
                                ErrorResponseMapper().generate(result.response).reason
                            )
                        )
                    else -> {}
                }
            }
    }

    private suspend fun storeDonation(donationValue: Long, donationId: String, ngoInfo: NgoInfo) = viewModelScope.launch {
        ngpInstitutionsUseCase.storeDonation(donationValue, donationId, ngoInfo)
            .collectLatest {
                _ngoInstitutionsChannel.sendInViewModelScope(
                    this@NGOInstitutionsViewModel,
                    NGOInstitutionsEvent.PaymentSuccess (
                        ngoInfo,
                        donationValue
                    )
                )
                clear()
            }
    }

    private fun clear() {
        selectedNgo = null
        valueToDonate = null
    }
}