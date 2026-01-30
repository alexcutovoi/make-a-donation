package com.app.makeadonation.ngocategories.domain.usecase

import com.app.makeadonation.ngocategories.data.mapper.NGOCategoryMapperResponse
import com.app.makeadonation.ngocategories.data.repository.NGOCategoriesRepository
import kotlinx.coroutines.flow.flow

class NgpCategoriesUseCaseImpl(
    private val ngoCategoriesRepository: NGOCategoriesRepository
) : NgoCategoriesUseCase {
    override suspend fun retrieveCategories() = flow {
        emit(
            ngoCategoriesRepository.retrieveCategories().map {
                NGOCategoryMapperResponse().generate(it)
            }
        )
    }
}