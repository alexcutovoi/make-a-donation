package com.app.makeadonation.ngocategories.domain.usecase

import com.app.makeadonation.ngocategories.domain.entity.NgoCategory
import kotlinx.coroutines.flow.Flow

interface NgoCategoriesUseCase {
    suspend fun retrieveCategories() : Flow<List<NgoCategory>>
}