package com.app.makeadonation.ngolistdonations.data.model

import com.google.gson.annotations.SerializedName

data class NgoInfoResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("info") val info: String,
    @SerializedName("imageLink") val imageLink: String,
    @SerializedName("companyId") val companyId: String,
)