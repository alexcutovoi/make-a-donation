package com.app.makeadonation.ngolistdonations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.MakeADonationApplication
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.onException
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import com.app.makeadonation.ngolistdonations.domain.usecase.NgoListDonationsUseCase
import com.app.makeadonation.payment.PaymentDispatcher
import com.app.makeadonation.payment.data.mapper.ErrorResponseMapper
import com.app.makeadonation.payment.data.mapper.ListOrdersMapper
import com.app.makeadonation.payment.domain.entity.Payment
import com.app.makeadonation.payment.domain.entity.PaymentResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NGOListDonationsViewModel(
    private val ngpListDonationsUseCase: NgoListDonationsUseCase
) : ViewModel() {
    private val _ngoListDonationsChannel = Channel<BaseEvent>()
    val ngoListDonationsChannel = _ngoListDonationsChannel

    private lateinit var selectedNgo: NgoInfo

    fun init() {
        listDonations()
        handlePayment()
    }

    fun cancelOrder(ident: String, payment: Payment) = viewModelScope.launch {
        payment.run {
            ngpListDonationsUseCase.cancelDonation(ident, cieloCode, authCode, amount)
                .collectLatest {
                    _ngoListDonationsChannel.sendInViewModelScope(
                        this@NGOListDonationsViewModel,
                        NGOListDonationsEvent.CancelDonation(it)
                    )
                }
        }
    }

    fun listDonations() = viewModelScope.launch {
        ngpListDonationsUseCase.listDonations(0, 5)
            .onStart {
                _ngoListDonationsChannel.sendInViewModelScope(
                    this@NGOListDonationsViewModel,
                    BaseEvent.ShowLoading
                )
            }
            .collectLatest {
                _ngoListDonationsChannel.sendInViewModelScope(
                    this@NGOListDonationsViewModel,
                    NGOListDonationsEvent.ListDonations(it)
                )
            }
    }

    private fun handlePayment() = viewModelScope.launch {
        PaymentDispatcher.results
            .collectLatest { data ->
                when (val result = ngpListDonationsUseCase.handlePaymentList(data)) {
                    is PaymentResult.ListOrdersSuccess -> {
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.ListOrdersSuccess(
                                ListOrdersMapper().generate(result.response)
                            )
                        )
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            BaseEvent.HideLoading
                        )
                    }

                    is PaymentResult.CancelledOrderSuccess -> {
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            MakeADonationApplication.getApplicationContext().run {
                                NGOListDonationsEvent.CancelledDonation(
                                    getString(
                                        R.string.warning
                                    ),
                                    getString(
                                        R.string.donation_successfully_canceled
                                    )
                                )
                            }
                        )
                    }

                    is PaymentResult.Cancel ->
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.PaymentError(
                                "Atenção", ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    is PaymentResult.Error ->
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.PaymentError(
                                "Atenção", ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    else -> {}
                }
            }
    }
}