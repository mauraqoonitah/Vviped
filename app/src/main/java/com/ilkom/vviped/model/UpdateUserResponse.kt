package com.ilkom.vviped.model

data class UpdateUserResponse(
    val error: Boolean,
    val message: String,
    val image: String,
    val fullname: String,
    val username: String,
    val profpic: String
)
