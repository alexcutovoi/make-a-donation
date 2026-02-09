package com.app.makeadonation.ngolistdonations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.common.sendInViewModelScope
import com.app.makeadonation.ngolistdonations.domain.entity.NgoDonationInfo
import com.app.makeadonation.ngolistdonations.domain.usecase.NgoListDonationsUseCase
import com.app.makeadonation.payment.PaymentDispatcher
import com.app.makeadonation.payment.data.mapper.ErrorResponseMapper
import com.app.makeadonation.payment.data.mapper.ListOrdersMapper
import com.app.makeadonation.payment.domain.entity.PaymentResult
import com.app.makeadonation.payment.domain.entity.Success
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NGOListDonationsViewModel(
    private val textProvider: TextProvider,
    private val ngoListDonationsUseCase: NgoListDonationsUseCase
) : ViewModel() {
    private val _ngoListDonationsChannel = Channel<BaseEvent>()
    val ngoListDonationsChannel = _ngoListDonationsChannel
    private var currentPage = 0
    private var maxNumOfItems = 5

    fun init() {
        listDonations()
        handlePayment()
    }

    fun cancelOrder(transactionId: String, ngoDonationInfo: NgoDonationInfo) = viewModelScope.launch {
        ngoDonationInfo.run {
            ngoListDonationsUseCase.cancelDonation(transactionId, operatorTransactionCode, authDonationCode, donatedValue)
                .collectLatest {
                    _ngoListDonationsChannel.sendInViewModelScope(
                        this@NGOListDonationsViewModel,
                        NGOListDonationsEvent.CancelDonation(it)
                    )
                }
        }
    }

    fun listDonations() = viewModelScope.launch {
        ngoListDonationsUseCase.listRegisteredDonations(currentPage, maxNumOfItems)
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
    
    fun donations(registeredDonations: List<Success>) = viewModelScope.launch {
        ngoListDonationsUseCase.listRegisteredDonationsFromServer(registeredDonations)
            .onCompletion {
                _ngoListDonationsChannel.sendInViewModelScope(
                    this@NGOListDonationsViewModel,
                    BaseEvent.HideLoading
                )
            }
            .collectLatest {
                _ngoListDonationsChannel.sendInViewModelScope(
                    this@NGOListDonationsViewModel,
                    NGOListDonationsEvent.ListOrdersSuccess(it)
                )
            }
    }

    private fun handlePayment() = viewModelScope.launch {
        PaymentDispatcher.results
            .collectLatest { data ->
                when (val result = ngoListDonationsUseCase.handlePaymentList(data)) {
                    is PaymentResult.ListOrdersSuccess -> {
                        donations(
                            ListOrdersMapper().generate(result.response).orders
                        )
                    }

                    is PaymentResult.CancelledOrderSuccess -> {
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.CancelledDonation(
                                textProvider.getText(R.string.warning),
                                textProvider.getText(R.string.donation_successfully_canceled)
                            )
                        )
                    }

                    is PaymentResult.Cancel ->
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.PaymentError(
                                textProvider.getText(R.string.warning),
                                ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    is PaymentResult.Error ->
                        _ngoListDonationsChannel.sendInViewModelScope(
                            this@NGOListDonationsViewModel,
                            NGOListDonationsEvent.PaymentError(
                                textProvider.getText(R.string.warning),
                                ErrorResponseMapper().generate(result.response).reason
                            )
                        )

                    else -> {}
                }
            }
    }
}