package com.app.makeadonation.ngocategories.data.model

import com.google.gson.annotations.SerializedName

data class NgoCategoryResponse(
    @SerializedName("id") val id: String,
    @SerializedName("imageLink") val imageLink: String,
    @SerializedName("name") val name: String
)
