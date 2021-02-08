package com.ilkom.vviped.model

import com.google.gson.annotations.SerializedName

data class DetectedItem(
    @field:SerializedName("link")
    val link: String? = null,
    @field:SerializedName("object_name")
    val object_name: String? = null
)
