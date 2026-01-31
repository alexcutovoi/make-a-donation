package com.app.makeadonation.ngodonationreceipt.di

import com.app.makeadonation.ngodonationreceipt.presentation.NGOReceiptViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object NgoReceiptModule {
    val module = module {
        viewModelOf(::NGOReceiptViewModel)
    }
}