package com.example.pagingcomposewine.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.pagingcomposewine.BuildConfig
import com.example.pagingcomposewine.data.local.Beer
import com.example.pagingcomposewine.data.local.BeerDatabase
import com.example.pagingcomposewine.data.remtoe.BeerApi
import com.example.pagingcomposewine.data.remtoe.BeerRemoteMediator
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BeerDatabase::class.java,
            name = "Beer.db"
        ).build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideBeerApi(): BeerApi {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(
        beerDatabase: BeerDatabase,
        beerApi: BeerApi,
    ): Pager<Int, Beer> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(
                beerDatabase = beerDatabase,
                beerApi = beerApi,
            ),
            pagingSourceFactory = {
                beerDatabase.beerDao.pagingSource()
            }
        )
    }
}
