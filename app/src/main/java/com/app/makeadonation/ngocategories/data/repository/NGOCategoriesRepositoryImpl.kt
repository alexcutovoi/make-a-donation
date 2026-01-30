package com.app.makeadonation.ngocategories.data.repository

import com.app.makeadonation.common.Utils
import com.app.makeadonation.ngocategories.data.model.NgoCategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NGOCategoriesRepositoryImpl : NGOCategoriesRepository{
    override suspend fun retrieveCategories() : List<NgoCategoryResponse>  = withContext(Dispatchers.IO) {
        runCatching {
            Utils.retrieveJson<List<NgoCategoryResponse>>("ong_categories.json")
        }.getOrThrow()
    }
}