package com.example.pagingcomposewine.data.remtoe

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pagingcomposewine.data.local.BeerDatabase
import com.example.pagingcomposewine.data.local.Beer
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDatabase: BeerDatabase,
    private val beerApi: BeerApi,
): RemoteMediator<Int, Beer>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Beer>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val beers = beerApi.getBeers(
                page = loadKey,
                pageCount = state.config.pageSize,
            )
            beerDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDatabase.beerDao.upsertAll(beers)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}