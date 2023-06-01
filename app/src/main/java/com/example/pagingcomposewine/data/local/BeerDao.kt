package com.example.pagingcomposewine.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BeerDao {

    @Upsert
    suspend fun upsertAll(beers: List<Beer>)

    @Query("SELECT * FROM Beer")
    fun pagingSource(): PagingSource<Int, Beer>

    @Query("DELETE FROM Beer")
    suspend fun clearAll()
}