package com.example.iconfinder.Network

import com.example.iconfinder.model.ApiResponse


import retrofit2.Response
import retrofit2.http.*

interface IconFinderService {

    @GET(ICONS_URL)
    suspend fun getIcons(@Query("query") query : String,
        @QueryMap params: Map<String, String>) : Response<ApiResponse>
}


