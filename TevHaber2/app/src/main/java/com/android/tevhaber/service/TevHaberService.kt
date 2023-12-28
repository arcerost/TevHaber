package com.android.tevhaber.service

import com.android.tevhaber.model.NewsResponse
import com.android.tevhaber.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TevHaberService {

    @Headers(API_KEY)
    @GET("top-headlines")
    suspend fun getCategoryTopHeadlines(@Query("category") category: String): NewsResponse

    @Headers(API_KEY)
    @GET("everything")
    suspend fun getEverything(@Query("q") query: String): NewsResponse

    @Headers(API_KEY)
    @GET("everything")
    suspend fun search(@Query("q") query: String, @Query("searchIn") searchIn: String): NewsResponse

}