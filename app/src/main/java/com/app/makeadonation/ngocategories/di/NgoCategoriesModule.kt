package com.app.makeadonation.ngocategories.di

import com.app.makeadonation.ngocategories.data.repository.NGOCategoriesRepository
import com.app.makeadonation.ngocategories.data.repository.NGOCategoriesRepositoryImpl
import com.app.makeadonation.ngocategories.domain.usecase.NgoCategoriesUseCase
import com.app.makeadonation.ngocategories.domain.usecase.NgpCategoriesUseCaseImpl
import com.app.makeadonation.ngocategories.presentation.NGOCategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object NgoCategoriesModule {
    val module = module {

        factoryOf(::NGOCategoriesRepositoryImpl) bind NGOCategoriesRepository::class
        factoryOf(::NgpCategoriesUseCaseImpl) bind NgoCategoriesUseCase::class

        viewModelOf(::NGOCategoriesViewModel)
    }
}