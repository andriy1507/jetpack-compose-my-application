package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.AUTH_HEADER
import com.spaceapps.myapplication.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.local.AuthTokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authTokenStorage: AuthTokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val builder = chain.request().newBuilder()
        authTokenStorage.getAuthToken()?.let { token ->
            builder.header(AUTH_HEADER, "$AUTH_HEADER_PREFIX $token")
        }
        return@runBlocking chain.proceed(builder.build())
    }
}
