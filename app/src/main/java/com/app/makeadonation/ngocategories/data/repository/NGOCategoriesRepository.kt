package com.app.makeadonation.ngocategories.data.repository

import com.app.makeadonation.ngocategories.data.model.NgoCategoryResponse

interface NGOCategoriesRepository {
    suspend fun retrieveCategories() : List<NgoCategoryResponse>
}