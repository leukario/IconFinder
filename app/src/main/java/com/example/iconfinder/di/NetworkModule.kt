package com.example.iconfinder.di

import com.example.iconfinder.network.AuthInterceptor
import com.example.iconfinder.network.IconFinderService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    private val httpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor("Bearer ", "X0vjEUN6KRlxbp2DoUkyHeM0VOmxY91rA6BbU5j3Xu6wDodwS0McmilLPBWDUcJ1"))

    @Singleton
    @Provides
    fun providesRetrofitInstance() : Retrofit {
        val build=httpClient.build()

        return Retrofit.Builder().baseUrl(com.example.iconfinder.network.BASE_URL)
            .client(build)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun providesIconFinderServices(retrofit: Retrofit) : IconFinderService{
        return retrofit.create(IconFinderService::class.java)
    }
}