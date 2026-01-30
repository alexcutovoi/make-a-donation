package com.app.makeadonation.ngocategories.data.mapper

import com.app.makeadonation.common.BaseMapper
import com.app.makeadonation.ngocategories.data.model.NgoCategoryResponse
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory

class NGOCategoryMapperResponse : BaseMapper<NgoCategoryResponse, NgoCategory> {
    override fun generate(input: NgoCategoryResponse) = NgoCategory(
        id = input.id,
        name = input.name,
        imageLink = input.imageLink
    )
}