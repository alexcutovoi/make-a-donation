package com.app.makeadonation.ngoinstitutions.data.model

data class NgoInfoRequest(
    val id: String,
    val name: String,
    val info: String,
    val imageLink: String,
    val companyId: String,
)