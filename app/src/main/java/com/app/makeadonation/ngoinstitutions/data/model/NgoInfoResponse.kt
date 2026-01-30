package com.app.makeadonation.ngoinstitutions.data.model

import com.google.gson.annotations.SerializedName

data class NgoInfoResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("info") val info: String,
    @SerializedName("imagelink") val imageLink: String,
    @SerializedName("companyId") val companyId: String,
)