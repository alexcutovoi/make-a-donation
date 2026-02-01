package com.app.makeadonation.ngolistdonations.di

import com.app.makeadonation.ngolistdonations.data.repository.NGOListDonationsRepository
import com.app.makeadonation.ngolistdonations.data.repository.NGOListDonationsRepositoryImpl
import com.app.makeadonation.ngolistdonations.domain.usecase.NgoListDonationsUseCase
import com.app.makeadonation.ngolistdonations.domain.usecase.NgpListDonationsUseCaseImpl
import com.app.makeadonation.ngolistdonations.presentation.NGOListDonationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object NgoListDonationsModule {
    val module = module {
        factoryOf(::NGOListDonationsRepositoryImpl) bind NGOListDonationsRepository::class
        factoryOf(::NgpListDonationsUseCaseImpl) bind NgoListDonationsUseCase::class
        viewModelOf(::NGOListDonationsViewModel)
    }
}