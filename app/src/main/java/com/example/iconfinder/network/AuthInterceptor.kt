package com.example.iconfinder.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenType: String, private val accessToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

                request = request.newBuilder().addHeader("Authorization", "Bearer X0vjEUN6KRlxbp2DoUkyHeM0VOmxY91rA6BbU5j3Xu6wDodwS0McmilLPBWDUcJ1").build()


        return chain.proceed(request)
    }
}