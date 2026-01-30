package com.app.makeadonation.ngoinstitutions.di

import com.app.makeadonation.ngocategories.presentation.NGOCategoriesViewModel
import com.app.makeadonation.ngoinstitutions.data.repository.NGOInstitutionsRepository
import com.app.makeadonation.ngoinstitutions.data.repository.NGOInstitutionsRepositoryImpl
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgoInstitutionsUseCase
import com.app.makeadonation.ngoinstitutions.domain.usecase.NgpInstitutionsUseCaseImpl
import com.app.makeadonation.ngoinstitutions.presentation.NGOInstitutionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object NgoInfoModule {
    val module = module {
        factoryOf(::NGOInstitutionsRepositoryImpl) bind NGOInstitutionsRepository::class
        factoryOf(::NgpInstitutionsUseCaseImpl) bind NgoInstitutionsUseCase::class
        viewModelOf(::NGOInstitutionsViewModel)
    }
}