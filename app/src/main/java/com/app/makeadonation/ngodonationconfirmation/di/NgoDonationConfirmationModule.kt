package com.app.makeadonation.ngodonationconfirmation.di

import com.app.makeadonation.ngodonationconfirmation.presentation.NGODonationConfirmationModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object NgoDonationConfirmationModule {
    val module = module {
        viewModelOf(::NGODonationConfirmationModel)
    }
}