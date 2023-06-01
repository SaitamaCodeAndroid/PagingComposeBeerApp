package com.example.pagingcomposewine.data.remtoe

import com.example.pagingcomposewine.data.local.Beer
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    @GET("beers")
    suspend fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
    ): List<Beer>

}