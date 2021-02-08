package com.ilkom.vviped.model

data class UploadResponse (
    val error: Boolean,
    val message: String,
    val id: Int,
    val image: String,
    val profpic: String
)