package com.app.makeadonation.ngocategories.presentation

import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory

class NGOCategoriesEvent : BaseEvent() {
    data class Categories(val categories: List<NgoCategory>) : BaseEvent()
}