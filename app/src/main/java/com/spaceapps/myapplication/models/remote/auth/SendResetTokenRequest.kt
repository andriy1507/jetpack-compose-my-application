package com.spaceapps.myapplication.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendResetTokenRequest(
    @Json(name = "email")
    val email: String
)
