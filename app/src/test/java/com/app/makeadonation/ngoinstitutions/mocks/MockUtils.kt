package com.app.makeadonation.ngoinstitutions.mocks

import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo

object MockUtils {
    fun getNgoInfo() = NgoInfo(
        "0",
        "Enviromental NGO",
        "Enviromental NGO NGO fights for environmental causes",
        "forest",
        ""
    )
}
