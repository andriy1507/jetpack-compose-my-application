package com.spaceapps.myapplication.models.remote.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SocialSignInRequest(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "device")
    val device: DeviceRequest
)
